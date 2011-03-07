/**
 * author: fwang2@ornl.gov
 */
package org.esgf.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.esgf.domain.NewsEntity;
import org.esgf.service.NewsService;
import org.esgf.util.ImageDataMultipartFileEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;


@Controller
@RequestMapping(value = "/admin/news")
@SessionAttributes("news")
public class NewsController {

    private final static Logger LOG = Logger.getLogger(NewsController.class);

    private Map<Long, NewsEntity> newsMap = new ConcurrentHashMap<Long, NewsEntity>();

    private NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        return "redirect:list";
    }

    @RequestMapping(method = RequestMethod.GET, value = "create")
    public String setupForm(Model model) {
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
        //this.newsMap.put(news.assignId(), news);
        newsService.saveNewsEntity(news);

        return "redirect:list";
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {        
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
        binder.registerCustomEditor(NewsEntity.class, new ImageDataMultipartFileEditor());
        binder.bind(request);

    }

    @RequestMapping(method = RequestMethod.GET, value = "list")
    public String list(Model model) {

        // List<NewsEntity> newsList = new
        // ArrayList<NewsEntity>(newsMap.values());
        List<NewsEntity> newsList = newsService.getNewsEntityAll();
        model.addAttribute("newsList", newsList);
        return "admin/news_list";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String getView(@PathVariable Long id, Model model) {
        NewsEntity news = this.newsMap.get(id);
        if (news == null) {
            throw new ResourceNotFoundException(id);
        }
        model.addAttribute(news);
        return "admin/news_list";
    }
}
