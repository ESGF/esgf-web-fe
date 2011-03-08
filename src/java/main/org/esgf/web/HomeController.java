package org.esgf.web;

import java.util.List;

import org.apache.log4j.Logger;
import org.esgf.domain.NewsEntity;
import org.esgf.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/home")
public class HomeController {

    private final static Logger LOG = Logger.getLogger(HomeController.class);

    private NewsService newsService;

    @Autowired
    public HomeController(NewsService newsService) {
        this.newsService = newsService;
    }
    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        List<NewsEntity> newsList = newsService.getNewsEntityAll();
        LOG.debug("Total news items: " + newsList.size());
        model.addAttribute("newsList", newsList);
        return "home";
    }
}
