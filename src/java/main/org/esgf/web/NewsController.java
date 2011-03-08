/**
 *
 * This controller handles "NewsEntity", add, list, remove
 *
 * author: fwang2@ornl.gov
 */

package org.esgf.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


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
        LOG.debug("create ... ");
        model.addAttribute("news", new NewsEntity());
        return "admin/news_createForm";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(HttpServletRequest request,
            @ModelAttribute("news") NewsEntity news, BindingResult result)
    {
        if (result.hasErrors()) {
            for (ObjectError error: result.getAllErrors()) {
                LOG.error(error);
            }
            return "redirect:create";
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile("imageFile");
        try {
            news.setImageFile(multipartFile.getBytes());
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        news.setImageFileName(multipartFile.getOriginalFilename());
        LOG.debug("Received: " + news.getImageFileName());
        newsService.saveNewsEntity(news);

        return "redirect:.";
    }

//    @InitBinder
//    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
//        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
//        binder.registerCustomEditor(NewsEntity.class, new ImageDataMultipartFileEditor());
//        binder.bind(request);
//
//    }


    @RequestMapping(method = RequestMethod.GET, value = "image/{id}")
    public void displayImage(@PathVariable Long id, Model model, HttpServletResponse response) {
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
            LOG.debug("News with id of " + id + "is missing");
        }
    }

}
