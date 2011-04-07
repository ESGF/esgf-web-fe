/*****************************************************************************
 * Copyright © 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * “Licensor”) hereby grants to any person (the “Licensee”) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organization’s name.  If the Software is protected by a proprietary
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
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.”
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

/**
 *
 * This controller handles "NewsEntity", add, list, remove
 *
 * @author Feiyi Wang (fwang2@ornl.gov)
 *
 */

package org.esgf.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.esgf.domain.NewsEntity;
import org.esgf.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping(value = "/news/*")
public class NewsController {

    private final static Logger LOG = Logger.getLogger(NewsController.class);

    private NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @RequestMapping("list")
    public String index(Model model) {
        List<NewsEntity> newsList = newsService.getNewsEntityAll();
        model.addAttribute("newsList", newsList);
        return "admin/news_list";

    }

    @RequestMapping(value="remove/{id}")
    public @ResponseBody void remove(@PathVariable Long id) {
        LOG.debug("new id [" + id + "] to be removed");
        newsService.removeNewsEntity(id);

        // if return a new page segment
        // return "admin/news_list";
    }

    @RequestMapping("create")
    public String setupForm(Model model) {
        LOG.debug("setup form ... ");
        model.addAttribute("news", new NewsEntity());
        return "admin/news_createForm";
    }

    @RequestMapping(value="save", method = RequestMethod.POST)
    public String processSubmit(
            @ModelAttribute("news") NewsEntity news,
            BindingResult result, @RequestParam("file") MultipartFile file)
    {

        LOG.debug("Saving news: " + news.getTitle());

        if (result.hasErrors()) {
            for (ObjectError error: result.getAllErrors()) {
                LOG.error(error);
            }

            return "redirect:create";
        }
        if (!file.isEmpty()) {
            try {
                news.setImageFile(file.getBytes());
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }

        news.setImageFileName(file.getOriginalFilename());
        LOG.debug("Received: " + news.getImageFileName());
        newsService.saveNewsEntity(news);

        return "redirect:/admin";
    }

    /*
    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
        binder.registerCustomEditor(NewsEntity.class, new ImageDataMultipartFileEditor());
        binder.bind(request);

    } */


    @RequestMapping(method = RequestMethod.GET, value = "image/{id}")
    public void displayImage(@PathVariable Long id, HttpServletResponse response) {
        LOG.debug("image id " + id);
        NewsEntity news = newsService.getNewsEntity(id);
        if (news != null) {
            try {
                FileCopyUtils.copy(news.getImageFile(), response.getOutputStream());
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }
        else {
            LOG.debug("News with id of [ " + id + " ] is missing");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value="show")
    public String slideShow(Model model) {
        List<NewsEntity> newsList = newsService.getNewsEntityAll();
        model.addAttribute("newsList", newsList);
        return "admin/news_show";
    }

}
