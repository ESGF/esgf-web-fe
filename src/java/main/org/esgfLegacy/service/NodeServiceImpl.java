/*****************************************************************************
 * Copyright 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * "Licensor") hereby grants to any person (the "Licensee") obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organization's name.  If the Software is protected by a proprietary
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
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy."
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
 

package org.esgfLegacy.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPath;

import org.esgfLegacy.NodeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



/**
 * This class provides node status update based on registry information
 *
 * @author Feiyi Wang
 *
 */

public class NodeServiceImpl implements NodeService {

    private static String NODE_EXPR = "//registry:Node";
    private static String NODE_NAME = "hostname";
    private static String NODE_IP = "ip";
    private final static Logger LOG = LoggerFactory.getLogger(NodeServiceImpl.class);

    private String regfile = "";
    private XPath xpath;
    private DocumentBuilder builder;
    private Document document;

    public NodeServiceImpl(String regfile) {

        xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext( new NamespaceContext() {
            public String getNamespaceURI( String prefix) {
                if ( prefix.equals("registry")) {
                    return "http://www.esgf.org/registry";
                }
                return null;
            }

            public String getPrefix( String namespaceURI) {
                if (namespaceURI.equals("http://www.esgf.org/registry")) {
                    return "registry";
                }
                return null;
            }

            public Iterator<String> getPrefixes (String namespaceURI) {
                ArrayList<String> list = new ArrayList<String>();
                if (namespaceURI.equals("http://www.esgf.org/registry")) {
                    list.add("registry");
                }
                return null;
            }
        });

        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);
            builder = domFactory.newDocumentBuilder();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.regfile = regfile;

    }

    /**
     * Analyze the document, retrieve and return the node list
     */
    @Override
    public List<NodeStatus> getLiveNodeList() {

        NodeList nodeList = null;
        List<NodeStatus> liveNodes = new ArrayList<NodeStatus>();
        InputSource isource = new InputSource(regfile);


        try {
            document = builder.parse(isource);

            Object result = xpath.evaluate(NODE_EXPR, document.getDocumentElement(), XPathConstants.NODESET);
            nodeList = (NodeList) result;

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            LOG.error("Input Source is not Valid, run empty list");
            liveNodes.add(new NodeStatus("localhost","127.0.0.1"));
            printNodeList(liveNodes);
            
            return liveNodes;

        }

        for (int i = 0; i < nodeList.getLength(); i++) {
            NamedNodeMap attrs = nodeList.item(i).getAttributes();

            Node hostname, hostip;
            String name, ip;

            hostname = attrs.getNamedItem(NODE_NAME);
            hostip = attrs.getNamedItem(NODE_IP);

            // IP must be present, otherwise, we will not add it to live node list
            if (hostip == null) continue;

            ip = hostip.getNodeValue();

            // Hostname is optional though it should be mandatory
            if (hostname == null)
                name = ip;
            else
                name = hostname.getNodeValue();

            liveNodes.add(new NodeStatus(name, ip));

        }
        
        
        //liveNodes.add(new NodeStatus("localhost","127.0.0.1"));
        //printNodeList(liveNodes);
        //LOG.debug("\n\n\n\n\n\n");
        //System.out.println(liveNodes.get(0).getNodeIp());
        //LOG.debug("\n\n\n\n\n\n");
        
        return liveNodes;
    }

    private static void printNodeList(List<NodeStatus> nodeList) {
        for (NodeStatus ns : nodeList )
            System.out.println("node:" + ns.getNodeName() + "; ip:" + ns.getNodeIp());
    }

    public static void main(String[] argv) throws Exception {

        NodeService ns = new NodeServiceImpl("file:///esg/config/registration.xml");
        printNodeList(ns.getLiveNodeList());
    }
}
