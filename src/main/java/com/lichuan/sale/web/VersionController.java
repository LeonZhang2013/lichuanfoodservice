package com.lichuan.sale.web;
import com.lichuan.sale.core.Result;
import com.lichuan.sale.core.ResultGenerator;
import com.lichuan.sale.model.Version;
import com.lichuan.sale.service.VersionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by leonZhang on 2018/05/13.
*/
@RestController
@RequestMapping("/version")
public class VersionController {
    @Resource
    private VersionService versionService;

    @PostMapping("/add")
    public Result add(Version version) {
        versionService.save(version);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        versionService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Version version) {
        versionService.update(version);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Version version = versionService.findById(id);
        return ResultGenerator.genSuccessResult(version);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Version> list = versionService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
