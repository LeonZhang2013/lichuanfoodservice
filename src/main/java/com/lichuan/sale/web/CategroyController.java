package com.lichuan.sale.web;
import com.lichuan.sale.core.Result;
import com.lichuan.sale.core.ResultGenerator;
import com.lichuan.sale.model.Categroy;
import com.lichuan.sale.service.CategroyService;
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
@RequestMapping("/categroy")
public class CategroyController {
    @Resource
    private CategroyService categroyService;

    @PostMapping("/add")
    public Result add(Categroy categroy) {
        categroyService.save(categroy);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        categroyService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Categroy categroy) {
        categroyService.update(categroy);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Categroy categroy = categroyService.findById(id);
        return ResultGenerator.genSuccessResult(categroy);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Categroy> list = categroyService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
