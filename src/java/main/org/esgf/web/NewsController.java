/**
 *
 * This controller handles "NewsEntity", add, list, remove
 *
 * author: fwang2@ornl.gov
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
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping(value = "/news")
public class NewsController {

    private final static Logger LOG = Logger.getLogger(NewsController.class);

    private NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        List<NewsEntity> newsList = newsService.getNewsEntityAll();
        model.addAttribute("newsList", newsList);
        return "admin/news_list";

    }

    @RequestMapping(method = RequestMethod.GET, value = "create")
    public String setupForm(Model model) {
        LOG.debug("setup form ... ");
        model.addAttribute("news", new NewsEntity());
        return "admin/news_createForm";
    }

    @RequestMapping(method = RequestMethod.POST)
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

        return "redirect:.";
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
