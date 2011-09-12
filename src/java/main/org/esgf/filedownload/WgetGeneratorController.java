
/*****************************************************************************
 * Copyright � 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * �Licensor�) hereby grants to any person (the �Licensee�) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organization�s name.  If the Software is protected by a proprietary
 * trademark owned by Licensor or the Department of Energy, then derivative
 * works of the Software may not be distributed using the trademark without
 * the prior written approval of the trademark owner.
 *
 * 2. Neither the names of Licensor nor the Department of Energy may be used
 * to endorse or promote products derived from this Software without their
 * specific prior written permission.
 *
 * 3. The Software, with or without modification, must include the following
 * acknowledgment:
 *
 *    "This product includes software produced by UT-Battelle, LLC under
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.�
 *
 * 4. Licensee is authorized to commercialize its derivative works of the
 * Software.  All derivative works of the Software must include paragraphs 1,
 * 2, and 3 above, and the DISCLAIMER below.
 *
 *
 * DISCLAIMER
 *
 * UT-Battelle, LLC AND THE GOVERNMENT MAKE NO REPRESENTATIONS AND DISCLAIM
 * ALL WARRANTIES, BOTH EXPRESSED AND IMPLIED.  THERE ARE NO EXPRESS OR
 * IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE,
 * OR THAT THE USE OF THE SOFTWARE WILL NOT INFRINGE ANY PATENT, COPYRIGHT,
 * TRADEMARK, OR OTHER PROPRIETARY RIGHTS, OR THAT THE SOFTWARE WILL
 * ACCOMPLISH THE INTENDED RESULTS OR THAT THE SOFTWARE OR ITS USE WILL NOT
 * RESULT IN INJURY OR DAMAGE.  The user assumes responsibility for all
 * liabilities, penalties, fines, claims, causes of action, and costs and
 * expenses, caused by, resulting from or arising out of, in whole or in part
 * the use, storage or disposal of the SOFTWARE.
 *
 *
 ******************************************************************************/

package org.esgf.filedownload;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.esgf.metadata.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * Implementation of a controller that generates wget scripts on the fly.  
 * A request is sent from the front end to generate this script.
 * Contained in its query string are the following parameters:
 * - id name of the dataset
 * - array of the individual file names contained in the batch of files requested for download
 * - a 'create' or 'delete' query variable (in lieu of http PUT and DELETE)
 *
 *
 * @author john.harney
 *
 */
@Controller
@RequestMapping("/wgetproxy")
public class WgetGeneratorController {

  
  private final static Logger LOG = Logger.getLogger(WgetGeneratorController.class);

  /**
   * 
   * @param request
   * @param response
   * @throws IOException
   * @throws JSONException
   * @throws ParserConfigurationException
   * @Deprecated
   */
  @RequestMapping(method=RequestMethod.GET)
  public @ResponseBody void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ParserConfigurationException {
    
    LOG.debug("doGet wgetproxy");
    createWGET(request, response);
    
  } //end doGet

  
  
  /** doPost(HttpServletRequest request, HttpServletResponse response) 
   * The wget file is primarily created through the post method
   * 
   * @param request 
   * @param response 
   * @throws IOException
   * @throws JSONException
   * @throws ParserConfigurationException
   */
  @RequestMapping(method=RequestMethod.POST)
  public @ResponseBody void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParserConfigurationException {
    
    LOG.debug("doPost wgetproxy");
    
    //When the type parameter is "create", call the script generator method
    if(request.getParameter("type").equals("create")) {
      createWGET(request, response);
    }
    
  } //end doPost

  
  private String headerString(String templateVersion) {
      String headerStr = "";
      
      headerStr += "##############################################################################\n\n\n";
      headerStr += "# ESG Federation download script\n";
      headerStr += "#\n";
      headerStr += "# Template version: " + templateVersion + "\n";
      headerStr += "# Generated by the all new ESGF Gateway\n";
      headerStr += "#";
      headerStr += "##############################################################################\n\n\n";
      
      return headerStr;
  }
  
  
  private static String envVariablesString() {
      String envVariablesStr = "";
      
      envVariablesStr += "esgf_download_script_version=\"0.0.1\"\n";
      envVariablesStr += "esgf_cert=${esgf_cert:-\"~/.esg/credentials.pem\"}\n";
      envVariablesStr += "esgf_private=${esgf_private:-\"~/.esg/credentials.pem\"}\n\n";
     
      
      return envVariablesStr;
  }

  private static String downloadFunctionString(String [] files) {
      String downloadFunctionStr = "";
      
      downloadFunctionStr += "esgf_download() {\n";
      
      //add the "child urls" to the wget script
      //the child_urls are the files that were selected in the datacart
      //probably need to change the name "child urls" to something more relevant
      for(int i=0;i<files.length;i++) {
        LOG.debug("CHILD_URL: " + files[i]);
        downloadFunctionStr += "\t((debug || dry_run)) && " +
                               "echo \"wget $@ --certificate ${esgf_cert} --private-key ${esgf_private} '" + files[i] + "'\"\n";
        downloadFunctionStr += "\t((!dry_run)) && " +
                               "echo \"wget $@ --certificate ${esgf_cert} --private-key ${esgf_private} '" + files[i] + "'\"\n";
        
      }
      
      downloadFunctionStr += "}\n";
     
      
      return downloadFunctionStr;
  }
  
  
  
  
  private static String mainFunctionString() {
      String mainFunctionStr = "";
      
      mainFunctionStr += "#Handle download script options\n";
      mainFunctionStr += "#Pass the rest directly to download command\n";
      mainFunctionStr += "main() {\n";
      
      mainFunctionStr += "\tlocal command_args=()\n";
      mainFunctionStr += "\twhile [-n \"${1}\" ]; do\n";
      mainFunctionStr += "\t\tlocal unshift=0\n";
      mainFunctionStr += "\t\tcase ${1} in\n";
      mainFunctionStr += "\t\t\t--debug)\n \t\t\t\tdebug=1\n\t\t\t\t;;\n";
      mainFunctionStr += "\t\t\t--dry_run)\n \t\t\t\tdry_run=1\n\t\t\t\t;;\n";
      mainFunctionStr += "\t\t\t--certificate)\n \t\t\t\tshift\n\t\t\t\tesgf_cert=${1}\n\t\t\t\t;;\";\n";
      mainFunctionStr += "\t\t\t--private-key)\n \t\t\t\tshift\n\t\t\t\tesgf_private=${1}\n\t\t\t\t;;\";\n";
      mainFunctionStr += "\t\t\t--output-file)\n " + 
                         "\t\t\t\t#Because args passed are applied to each individual\n" + 
                         "\t\t\t\t#download we don't want to support this option in\n" + 
                         "\t\t\t\t#which case the output would be written over and over\n" + 
                         "\t\t\t\t#to the single file specified.  So this option is, in\n" + 
                         "\t\t\t\t#the context of this script, deamed unsupported.\n" + 
                         "\t\t\t\techo \"Unsupported option: --output-file\"\n" + 
                         "\t\t\t\texit 1\n\t\t\t\t;;\n"; 
      mainFunctionStr += "\t\t\t--help)\n " + 
                         "\t\t\t\techo \"ESGF dataset download script\"\n" + 
                         "\t\t\t\techo \"Version ${esgf_download_script_version}\"\n" + 
                         "\t\t\t\techo \n" + 
                         "\t\t\t\techo \" usage: $0 [--debug] [--dry-run] [--certificate <certfile>] [--private-key <private file>]\"\n" + 
                         "\t\t\t\techo \" (all other args are passed through args to wget command) \"\n" + 
                         "\t\t\t\techo \" (use --help --full to additionally see wget's help) \"\n" + 
                         "\t\t\t\techo \n" + 
                         "\t\t\t\tshift && [ \"$1\" = \"--full\" ] && echo \"$(wget --help)\"\n" + 
                         "\t\t\t\texit 0\n" + 
                         "\t\t\t\t;;\n";
      mainFunctionStr += "\t\t\t*)\n " + 
                         "\t\t\t\tcommand_args=(\"{command_args[@]} ${1}\") \n" + 
                         "\t\t\t\t;;\n";
      mainFunctionStr += "\t\tesac\n";
      mainFunctionStr += "\t\tshift\n";
      mainFunctionStr += "\tdone\n";
      mainFunctionStr += "\tesgf_download ${command_args[@]}\n";
      mainFunctionStr += "}\n\n";
      mainFunctionStr += "main $@\n";
              
      
      
      
      mainFunctionStr += "\n";
      
      
      return mainFunctionStr;
  }
  
  
  /** createWGET(HttpServletRequest request, HttpServletResponse response)
   * 
   * 
   * @param request passed from the doPost method
   * @param response x-sh response type 
   */
  private void createWGET(HttpServletRequest request, HttpServletResponse response) throws IOException, ParserConfigurationException {
    
    // Create the file name
    // The file name is the id of the dataset + ".sh"
    String filename = request.getParameter("id") + ".sh";
    String security = request.getParameter("security");
    
    LOG.debug("filename = " + filename);
    LOG.debug("security->" + security);
    queryStringInfo(request);
    
    // create content of the wget script
    String wgetText = "#!/bin/sh\n";
    
    if(security.equalsIgnoreCase("wgetv1")) {
        wgetText += "# ESG Federation download script\n";
        wgetText += "#\n";
        wgetText += "# Template version: 0.2\n";
        wgetText += "# Generated by the all new ESGF Gateway\n";
        wgetText += "#";
        wgetText += "##############################################################################\n\n\n";
        wgetText += "download() {\n";

        //add the "child urls" to the wget script
        //the child_urls are the files that were selected in the datacart
        //probably need to change the name "child urls" to something more relevant
        for(int i=0;i<request.getParameterValues("child_url").length;i++) {
          LOG.debug("CHILD_URL: " + request.getParameterValues("child_url")[i]);
          wgetText += "\twget " 
                   + "--certificate ~/.esg/credentials.pem --private-key ~/.esg/credentials.pem "
                   + "'" + request.getParameterValues("child_url")[i] + "'\n";
        }
        wgetText += "}\n";
        wgetText += "#\n# MAIN \n#\n";
        wgetText += "download\n";

        
    } else if(security.equalsIgnoreCase("wgetv2")){
        
        wgetText += this.ftlScript();
        

    }
    else if(security.equalsIgnoreCase("wgetv3")) {
        wgetText += "#!/bin/bash\n\n";
        
        //add the header
        wgetText += this.headerString("0.2");
        
        //add the environment variables
        wgetText += envVariablesString();
        
        //add the download function
        wgetText += downloadFunctionString(request.getParameterValues("child_url"));

        wgetText += "#\n# MAIN \n#\n";
        
        //add the main function
        wgetText += mainFunctionString();


        wgetText += "exit 0\n";
        
    }
    
    		
    
    //attach the sh file extension to the response
    response.setContentType("text/x-sh");
    response.addHeader("Content-Disposition", "attachment; filename=" + filename );
    response.setContentLength((int) wgetText.length());
   
    PrintWriter out = response.getWriter();
    out.print(wgetText);

    LOG.debug("Finishing writing wget stream");

  } 
  
  
  
  //experimental script
  private String ftlScript() {
    // TODO Auto-generated method stub
      String ftlText = "";
      
      
      ftlText += "#\n";
      ftlText += "# Script generated user OpenID: ${downloadScriptData.user.openid}\n";
      ftlText += "#\n";
      ftlText += "<#-- TODO: Download and launch MyProxyLogon script -->\n";
      ftlText += "<#-- TODO: Report ftl failures -->\n";
      ftlText += "#\n";
      ftlText += "<#if downloadScriptData.hasCertificateDownloads()>\n";
      ftlText += "##############################################################################\n";
      ftlText += "#\n";
      ftlText += "# Your download selection includes data secured using ESG\n";
      ftlText += "# certificate-based security.  In order to access the download URLs\n";
      ftlText += "# you must first obtain a credentials file from your home Gateway's\n";
      ftlText += "# MyProxy server.\n";
      ftlText += "#\n";
      ftlText += "# If you don't already have a myproxy client you can download the\n";
      ftlText += "# MyProxyLogon Java client from\n";
      ftlText += "#   ${downloadScriptData.gateway.baseURL}/webstart/myProxyLogon/MyProxyLogon-ESG.jar\n";
      ftlText += "#\n";
      ftlText += "# Then execute it as follows:\n";
      ftlText += "#  $ java -jar MyProxyLogon-ESG.jar -u <username>\\ \n";
      ftlText += "#         -h ${downloadScriptData.myProxyEndpoint.host}\\ \n";
      ftlText += "#         -p ${downloadScriptData.myProxyEndpoint.port?c}\\\n";
      ftlText += "#\n";
      ftlText += "# Further information is available at\n";
      ftlText += "#   ${downloadScriptData.gateway.baseURL}/help/download-help.htm\n";
      ftlText += "#\n";
      ftlText += "##############################################################################\n";
      ftlText += "\n";
      ftlText += "##############################################################################\n";
      ftlText += "#\n";
      ftlText += "# Script defaults\n";
      ftlText += "#\n";
      ftlText += "${r\n";
      ftlText += "# ESG_HOME should point to the directory containing ESG credentials.\n";
      ftlText += "#   Default is $HOME/.esg.\n";
      ftlText += "ESG_HOME=${ESG_HOME:-$HOME/.esg}\n";
      ftlText += "ESG_CREDENTIALS=${X509_USER_PROXY:-$ESG_HOME/credentials.pem}\n";
      ftlText += "ESG_CERT_DIR=${X509_CERT_DIR:-$ESG_HOME/certificates}\n";
      ftlText += "COOKIE_JAR=$ESG_HOME/cookies\n";
      ftlText += "CERT_EXPIRATION_WARNING=$((60 * 60 * 1))    #One hour (in seconds)\n";
      ftlText += "# Configure checking of server SSL certificates.\n";
      ftlText += "#   Disabling server certificate checking can resolve problems with myproxy\n";
      ftlText += "#   servers being out of sync with datanodes.\n";
      ftlText += "CHECK_SERVER_CERT=${CHECK_SERVER_CERT:-Yes}\n";
      ftlText += "\"}\n";
      ftlText += "\n";
      ftlText += "</#if>\n";
      ftlText += "usage() {\n";
      ftlText += "\techo \"Usage: $(basename $0) [flags]\"\n";
      ftlText += "\techo \"Flags is one of:\"\n";
      ftlText += "sed -n '/^while getopts/,/^done/  s/^\\([^)]*\\)[^#]*#\\(.*$\\)/\\1 - \\2/p' $0\n";
      ftlText += "}\n";
      ftlText += "#defaults\n";
      ftlText += "debug=0\n";
      ftlText += "clean_work=1\n";
      ftlText += "\n";
      ftlText += "#parse flags\n";
      ftlText += "<#if downloadScriptData.hasCertificateDownloads()>\n";
      ftlText += "while getopts ':c:pdq' OPT; do\n";
      ftlText += "<#else>\n";
      ftlText += "while getopts ':pdq' OPT; do\n";
      ftlText += "</#if>\n";
      ftlText += "    case $OPT in\n";
      ftlText += "<#if downloadScriptData.hasCertificateDownloads()>\n";
      ftlText += "        c) ESG_CREDENTIALS=\"$OPTARG\";;  #<cert> use this certificate for authentication.\n";
      ftlText += "</#if>\n";
      ftlText += "        p) clean_work=0;;       #   preserve data that failed checksum\n";
      ftlText += "        d) debug=1;;            #   display debug information\n";
      ftlText += "        q) quiet=1;;            #   be less verbose\n";
      ftlText += "        \\?) echo \"Unknown option '$OPTARG'\" >&2 && usage && exit 1;;\n";
      ftlText += "        \\:) echo \"Missing parameter for flag '$OPTARG'\" >&2 && usage && exit 1;;\n";
      ftlText += "    esac\n";
      ftlText += "done\n";
      ftlText += "shift $(($OPTIND - 1))\n";
      ftlText += "\n";
      ftlText += "##############################################################################\n";
      ftlText += "<#if downloadScriptData.hasCertificateDownloads()>\n";
      ftlText += "# Retrieve ESG credentials (not done yet)\n";
      ftlText += "get_credentials() {\n";
      ftlText += "    cat <<EOF\n";
      ftlText += "Your download selection includes data secured using ESG\n";
      ftlText += "certificate-based security.  In order to access the download URLs\n";
      ftlText += "you must first obtain a credentials file from your home Gateway's\n";
      ftlText += "MyProxy server at ${downloadScriptData.myProxyEndpoint.host}:${downloadScriptData.myProxyEndpoint.port?c}\n";
      ftlText += "\n";
      ftlText += "If you don't already have a myproxy client you can download the\n";
      ftlText += "MyProxyLogon Java client from\n";
      ftlText += "${downloadScriptData.gateway.baseURL}/webstart/myProxyLogon/MyProxyLogon-ESG.jar\n";
      ftlText += "Then execute it as follows:\n";
      ftlText += "$ java -jar MyProxyLogon-ESG.jar -u <username> -h ${downloadScriptData.myProxyEndpoint.host} -p ${downloadScriptData.myProxyEndpoint.port?c}\n";
      ftlText += "Further information is available at\n";
      ftlText += "  ${downloadScriptData.gateway.baseURL}/help/download-help.htm\n";
      ftlText += "EOF\n";
      ftlText += "    exit 1\n";
      ftlText += "}\n";
      ftlText += "\n";
      ftlText += "# check the certificate validity\n";
      ftlText += "check_cert() {\n";
      ftlText += "#chek openssl and certificate\n";
      ftlText += "    if (which openssl &>/dev/null); then\n";
      ftlText += "        if ! openssl x509 -checkend 0 -noout -in $ESG_CERT; then\n";
      ftlText += "            echo \"The Certificate has expired, please renew.\"\n";
      ftlText += "            return 1\n";
      ftlText += "        else\n";
      ftlText += "            if ! openssl x509 -checkend $CERT_EXPIRATION_WARNING -noout -in $ESG_CERT; then\n";
      ftlText += "                echo \"The certificate expires in less than $((CERT_EXPIRATION_WARNING / 60 / 60)) hour(s), please renew.\"\n";
      ftlText += "                return 2\n";
      ftlText += "            fi\n";
      ftlText += "        fi\n";
      ftlText += "    fi\n";
      ftlText += "}\n";
      ftlText += "\n";
      ftlText += "#\n";
      ftlText += "# Detect ESG credentials\n";
      ftlText += "#\n";
      ftlText += "find_credentials() {\n";
      ftlText += "\n";
      ftlText += "    if [[ -f \"$ESG_CREDENTIALS\" ]]; then\n";
      ftlText += "        # file found, proceed.\n";
      ftlText += "        ESG_CERT=\"$ESG_CREDENTIALS\"\n";
      ftlText += "        ESG_KEY=\"$ESG_CREDENTIALS\"\n";
      ftlText += "    elif [[ -f \"$X509_USER_CERT\" && -f \"$X509_USER_KEY\" ]]; then\n";
      ftlText += "        # second try, use these certificates.\n";
      ftlText += "        ESG_CERT=\"$X509_USER_CERT\"\n";
      ftlText += "        ESG_KEY=\"$X509_USER_KEY\"\n";
      ftlText += "    else\n";
      ftlText += "        # If credentials are not present exit\n";
      ftlText += "        echo \"No ESG Credentials found in $ESG_CREDENTIALS\" >&2\n";
      ftlText += "            get_credentials\n";
      ftlText += "    fi\n";
      ftlText += "\n";
      ftlText += "\n";
      ftlText += "#chek openssl and certificate\n";
      ftlText += "    if (which openssl &>/dev/null); then\n";
      ftlText += "        if ( openssl version | grep 'OpenSSL 1\\.0' ); then\n";
      ftlText += "\n";
      ftlText += "\n";
      ftlText += "            echo '** WARNING: ESGF Host certificate checking is not compatible with OpenSSL 1.0+'\n";
      ftlText += "            echo '**          ESGF Certificate directory is not being consulted'\n";
      ftlText += "            #Drop CA check, because it won't work as hashing of certs has change.\n";
      ftlText += "            unset CHECK_SERVER_CERT\n";
      ftlText += "        fi\n";
      ftlText += "        check_cert || { (($?==1)); exit 1; }\n";
      ftlText += "    fi\n";
      ftlText += "\n";
      ftlText += "    if [[ $CHECK_SERVER_CERT == \"Yes\" ]]; then\n";
      ftlText += "        [[ -d \"$ESG_CERT_DIR\" ]] || { echo \"CA certs not found. Aborting.\"; exit 1; }\n";
      ftlText += "        PKI_WGET_OPTS=\"--ca-directory=$ESG_CERT_DIR\"\n";
      ftlText += "    fi\n";
      ftlText += "\n";
      ftlText += "    PKI_WGET_OPTS=\"$PKI_WGET_OPTS --certificate=$ESG_CERT --private-key=$ESG_KEY --save-cookies=$COOKIE_JAR --load-cookies=$COOKIE_JAR\"\n";
      ftlText += "}\n";
      ftlText += "</#if>\n";
      ftlText += "\n";
      ftlText += "download() {\n";
      ftlText += "<#if downloadScriptData.hasCertificateDownloads()>\n";
      ftlText += "    wget=\"wget -c $PKI_WGET_OPTS\"\n";
      ftlText += "<#else>\n";
      ftlText += "    ftl=\"wget -c\"\n";
      ftlText += "</#if>\n";
      ftlText += "    ((quiet)) && wget=\"$wget -q\"\n";
      ftlText += "    while read line\n";
      ftlText += "    do\n";
      ftlText += "        # read csv here document into proper variables\n";
      ftlText += "        eval $(awk -F \"' '\" '{$0=substr($0,2,length($0)-2); $3=tolower($3); print \"file=\\\"\"$1\"\\\";url=\\\"\"$2\"\\\";chksum_type=\\\"\"$3\"\\\";chksum=\\\"\"$4\"\\\"\"}' <(echo $line) )\n";
      ftlText += "        #Process the file\n";
      ftlText += "        echo -n \"$file ...\"\n";
      ftlText += "        [[ -f \"$file\" ]] && echo -n \"  continuing download ...\" || echo -n \"  starting downloading ...\"\n";
      ftlText += "        while : ; do\n";
      ftlText += "                # (if we had the file size, we could check before trying to complete)\n";
      ftlText += "                $wget -O \"$file\" $url || { failed=1; break; }\n";
      ftlText += "                #check if file is there\n";
      ftlText += "                if [[ -f $file ]]; then\n";
      ftlText += "                        ((debug)) && echo file found\n";
      ftlText += "                        if ! check_chksum \"$file\" $chksum_type $chksum; then\n";
      ftlText += "                                echo \"  $chksum_type failed!\"\n";
      ftlText += "                                if ((clean_work)); then\n";
      ftlText += "                                        rm $file\n";
      ftlText += "                                        #try again\n";
      ftlText += "                                        echo -n \"  re-downloading...\"\n";
      ftlText += "                                        continue\n";
      ftlText += "                                else\n";
      ftlText += "                                        echo \"  don't use -p or remove manually.\"\n";
      ftlText += "                                fi\n";
      ftlText += "                        else\n";
      ftlText += "                                echo \"  $chksum_type ok. done!\"\n";
      ftlText += "                        fi\n";
      ftlText += "                fi\n";
      ftlText += "                #done!\n";
      ftlText += "                break\n";
      ftlText += "        done\n";
      ftlText += "        if ((failed)); then\n";
      ftlText += "            echo \"download failed\"\n";
      ftlText += "<#if downloadScriptData.hasCertificateDownloads()>\n";
      ftlText += "            # most common failure is certificate expiration, so check this\n";
      ftlText += "            check_cert\n";
      ftlText += "</#if>\n";
      ftlText += "            unset failed\n";
      ftlText += "        fi\n";
      ftlText += "    done <<EOF--dataset.file.url.chksum_type.chksum\n";
      ftlText += "<#list downloadScriptData.retrievableFiles as fileDownload>\n";
      ftlText += "'${fileDownload.fileAccessPoint.logicalFile.name}' '${fileDownload.downloadURI}' '${(fileDownload.fileAccessPoint.logicalFile.checksums[0].algorithm)!\"\"}' '${(fileDownload.fileAccessPoint.logicalFile.checksums[0].checksum)!\"\"}'\n";
      ftlText += "</#list>\n";
      ftlText += "EOF--dataset.file.url.chksum_type.chksum\n";
      ftlText += "\n";
      ftlText += "}\n";
      ftlText += "\n";
      ftlText += "#\n";
      ftlText += "# MAIN\n";
      ftlText += "#\n";
      ftlText += "echo \"Running $(basename $0) version: $version\"\n";
      ftlText += "<#if downloadScriptData.hasCertificateDownloads()>\n";
      ftlText += "find_credentials\n";
      ftlText += "</#if>\n";
      ftlText += "download\n";  
      
    return ftlText;
  }






  /**
   * queryStringInfo(HttpServletRequest request)
   * Private method printing out the contents of the request.  Used mainly for debugging.
   * 
   * @param request
   */
  private void queryStringInfo(HttpServletRequest request) {
    
    LOG.debug("Query parameters");
    LOG.debug("\tId");
    LOG.debug("\t\t" + request.getParameterValues("id")[0]);
    LOG.debug("\tType");
    LOG.debug("\t\t" + request.getParameterValues("type")[0]);
    LOG.debug("\tChild urls");
    for(int i=0;i<request.getParameterValues("child_url").length;i++) {
      LOG.debug("\t\t" + request.getParameterValues("child_url")[i]);
    }
    LOG.debug("\tChild ids");
    for(int i=0;i<request.getParameterValues("child_id").length;i++) {
      LOG.debug("\t\t" + request.getParameterValues("child_id")[i]);
    }
    
  } //end queryStringInfo

  
  
} //end servlet class