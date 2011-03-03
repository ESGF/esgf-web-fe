/**
 * author: fwang2@ornl.gov
 */
package org.esgf.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.esgf.domain.NewsEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/admin/news")
public class NewsController {

    private final static Logger LOG = Logger.getLogger(NewsController.class);

    private Map<Long, NewsEntity> newsMap = new ConcurrentHashMap<Long, NewsEntity>();

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        return "redirect:/admin/news/list";
    }

    @RequestMapping(method = RequestMethod.GET, value = "create")
    public String createForm(Model model) {
        model.addAttribute("news", new NewsEntity());
        return "admin/news_createForm";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid NewsEntity news, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/news_createForm";
        }

        this.newsMap.put(news.assignId(), news);
        return "redirect:/admin/news/list";
    }

    @RequestMapping(method = RequestMethod.GET, value = "list")
    public String list(Model model) {

        List<NewsEntity> newsList = new ArrayList<NewsEntity>(newsMap.values());

        model.addAttribute("newsList", newsList);
        return "admin/news_view";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String getView(@PathVariable Long id, Model model) {
        NewsEntity news = this.newsMap.get(id);
        if (news == null) {
            throw new ResourceNotFoundException(id);
        }
        model.addAttribute(news);
        return "admin/news_view";
    }
}
