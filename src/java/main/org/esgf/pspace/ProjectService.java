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

package org.esgf.pspace;

import java.text.NumberFormat;
import java.util.List;
import javax.xml.transform.Source;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathOperations;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * 
 * <response>
 *      <lst name="responseHeader">
 *      ...
 *      </lst>
 *      
 *      <lst name="params">
 *      ...
 *      </lst>
 *      
 *      <result name="response" numFound="xxx" start="0" />
 *      
 *      <lst name="facet_counts">
 *      
 *          <lst name="facet_queries" />
 *          <lst name="facet_fields">
 *              <lst name="project">
 *                  <int name="c-lamp">1613</int>
 *                  <int name="obs4MIPs">35</int>
 *                  ...
 *              </lst>
 *          </lst>
 *      </lst>
 *          
 * <response>
 * 
 * 
 * @author Feiyi Wang
 *
 */

@Component
public class ProjectService {

    private String baseSolr = "http://localhost:8983/solr/select/?";
    private XPathOperations xpathTemplate = new Jaxp13XPathTemplate();
    
    /**
     * Retrieve a list of facets value and its counts.<p>
     * 
     * This is a kludgy implementation in the way we parse and retrieve 
     * the specific field. The return is a list of string, and each string is composed 
     * by two fields separated by special character |. The first field is the the name 
     * of the facet, and second field is the count
     * 
     * 
     * @param facet
     * @return
     */

    public List<String> retrieveFacets(String facet) {
        Source source = new RestTemplate().getForObject(
                baseSolr + "q=*:*&facet=true&facet.field={facet}&rows=0", Source.class, facet);
        
        assert source != null;
        
        // select all <int> elements, and map each element to a String object 
        List<String> aList = xpathTemplate.evaluate("//lst[@name='project']/int", source, 
                new NodeMapper<String>() {
                
                @Override
                public String mapNode(Node node, int i) throws DOMException {
                    Element intNode = (Element) node;
                    return intNode.getAttribute("name") + "|" + intNode.getTextContent();
                 
            }
            
        });
        
        return aList;
    }
    
    /**
     * Extract numFound from Solr Response
     * 
     * @param source
     * @return
     */
    private List<String> extractNumFound(Source source) {
        // select all <int> elements, and map each element to a String object 
        List<String> aList = xpathTemplate.evaluate("//result[@name='response']", source, 
                new NodeMapper<String>() {
                
                @Override
                public String mapNode(Node node, int i) throws DOMException {
                    Element intNode = (Element) node;
                    return intNode.getAttribute("numFound");
                 
            }
            
        });
        return aList;
    }
    
    
    
    
    /**
     * 
     * Extract number of dasetset contained within a project
     * 
     * <result name="response" numFound="28" ... </result>
     * 
     * 
     * @param project
     * @return
     */
    public String retrieveNumOfDataset(String project) {
        Source source = new RestTemplate().getForObject(
                baseSolr + "q=*:*&fq=type:Dataset&fq=project:{project}&rows=0", Source.class, project);
        
        assert source != null;
        
        List<String> aList = extractNumFound(source);
        
        assert (aList != null) && (! aList.isEmpty());
        
        return aList.get(0);
    }

    /**
     * 
     * Extract number of files contained within a project
     * 
     * <result name="response" numFound="28" ... </result>
     * 
     * 
     * @param project
     * @return
     */
    public String retrieveNumOfFile(String project) {
        Source source = new RestTemplate().getForObject(
                baseSolr + "q=*:*&fq=type:File&fq=project:{project}&rows=0", Source.class, project);
        
        assert source != null;
        
        List<String> aList = extractNumFound(source);
        
        assert (aList != null) && (! aList.isEmpty());
        
        return aList.get(0);
    }

    
    
    /**
     * 
     * Given a project String, return the readme content as a String.
     * It is assumed and expected that only one such project meta doc
     * will be found and processed.
     * 
     * @param project
     * @return
     */
    public String retrieveReadme(String project) {
        Source source = new RestTemplate().getForObject(
                baseSolr + "q=*:*&fq=type:project.meta&fq=id:{project}.meta", Source.class, project);
        
        assert source != null;
        
        List<String> aList = xpathTemplate.evaluate("//doc/arr[@name='readme.content']/str",
                source, new NodeMapper<String>() {
                @Override
                public String mapNode(Node node, int i) throws DOMException {
                    Element intNode = (Element) node;
                    return intNode.getTextContent();
                }
        });
        
        assert ! aList.isEmpty();
        
        return aList.get(0);
    }

    private String formatSize(long longSize, int decimalPos)
    {
       NumberFormat fmt = NumberFormat.getNumberInstance();
       if (decimalPos >= 0)
       {
          fmt.setMaximumFractionDigits(decimalPos);
       }
       final double size = longSize;
       double val = size / (1024 * 1024);
       if (val > 1)
       {
          return fmt.format(val).concat(" MB");
       }
       val = size / 1024;
       if (val > 10)
       {
          return fmt.format(val).concat(" KB");
       }
       return fmt.format(val).concat(" bytes");
    }
    
    
    /**
     * Retrieve total data size published under a given project
     * 
     * This implementation assumes that a dataset level "size" is tallyed.
     * 
     * @param project
     * @return
     */
    public String retrieveTotalSize(String project) {
        String numOfRows = retrieveNumOfDataset(project);
        
        Source source = new RestTemplate().getForObject(
                baseSolr + "q=*:*&fq=type:Dataset&fq=project:{project}&rows={numOfRows}", 
                Source.class, project, numOfRows);
        
        assert source != null;

        List<String> aList = xpathTemplate.evaluate("//doc/arr[@name='size']/str",
                source, new NodeMapper<String>() {
                @Override
                public String mapNode(Node node, int i) throws DOMException {
                    Element intNode = (Element) node;
                    return intNode.getTextContent();
                }
        });
        
        long totalSize = 0L;
        
        for (String s : aList) {
            totalSize += Long.parseLong(s);
        }
        
        return formatSize(totalSize, 2);
    }
    public boolean existsProject(String project) {
        List<String> plist = retrieveFacets("project");
        
        for (String s: plist) {
            String[] p = s.split("\\|");
            if (p[0].trim().equalsIgnoreCase(project))
                return true;
        }
        
        return false;
     }
    
    public static void main(String[] argv) {
        ProjectService ps = new ProjectService();
        List<String> aList  = ps.retrieveFacets("project");
        System.out.println(aList);
        
        System.out.println(ps.retrieveNumOfDataset("c-lamp"));
        //System.out.println(ps.retrieveReadme("c-lamp"));
        System.out.println(ps.retrieveNumOfFile("c-lamp"));
        
    }
}
