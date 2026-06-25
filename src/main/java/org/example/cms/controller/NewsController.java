package org.example.cms.controller;


import org.example.cms.entity.News;
import org.example.cms.entity.Page;
import org.example.cms.mapper.NewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsMapper newsMapper;

    @Value("${my.upload.base_path}")
    public String UPLOAD_DIR;

    @GetMapping("/newsList")
    public Page<News> newsList(@RequestParam int currentPage,
                               @RequestParam int rows,
                               @RequestParam(required = false) Integer category) {  // 新增参数
        int start = (currentPage - 1) * rows;
        int total;
        List<News> data;

        if (category != null) {
            // 按分类查询
            total = newsMapper.countByCategory(category);
            data = newsMapper.getNewsByCategory(category, start, rows);
        } else {
            // 原逻辑：查询全部
            total = newsMapper.newsCount();
            data = newsMapper.newsList(start, rows);
        }

        int totalPages = total % rows == 0 ? total / rows : total / rows + 1;
        Page<News> newsPage = new Page<>();
        newsPage.setTotal(total);
        newsPage.setTotalPage(totalPages);
        newsPage.setData(data);
        return newsPage;
    }

    @PostMapping("/saveNews")
    public String saveNews(@RequestBody News news){
        System.out.println("新闻信息: " + news);
        newsMapper.insert(news);
        return "保存成功";
    }
    @PostMapping("/uploadImg")
    public Map<String, Object> uploadImg(@RequestParam("wangeditor-uploaded-image") MultipartFile file){
        System.out.println("文件的名字： "+file.getOriginalFilename());
        File f =new File(UPLOAD_DIR);
        if(!f.exists()){
            f.mkdirs();
        }
        UPLOAD_DIR  =  ensureTrailingSlash(UPLOAD_DIR);
        System.out.println("本地地址： " + UPLOAD_DIR);

        String path=UPLOAD_DIR+file.getOriginalFilename();
        File tmp=new File(path);
        try {
            file.transferTo(tmp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //返回固定格式给前端
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> dataMap=new HashMap<>();
        dataMap.put("url", "http://localhost:9002/upload/"+file.getOriginalFilename());

        result.put("errno",0);
        result.put("data",dataMap);
        return result;

    }
    private String ensureTrailingSlash(String path) {
        if (path == null || path.isEmpty()) {
            return "/";
        }
        return path.endsWith("/") ? path : path + "/";
    }
    @DeleteMapping("/deleteNews/{id}")
    @Transactional
    public String deletePermission(@PathVariable Long id) {
        try {
            int result = newsMapper.deleteById(id);
            if (result > 0) {
                return "删除成功";
            } else {
                return "删除失败：未找到对应记录";
            }
        } catch (Exception e) {
            throw new RuntimeException("删除失败：" + e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public News getNewsById(@PathVariable Long id) {
        return newsMapper.selectById(id);
    }

    @PutMapping("/audit/{id}")
    public String auditNews(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        News news = newsMapper.selectById(id);
        if (news == null) {
            throw new RuntimeException("新闻不存在");
        }
        news.setStatus(status);
        news.setUpdateTime(new Date());
        newsMapper.updateById(news);
        return "审核状态已更新";
    }
}