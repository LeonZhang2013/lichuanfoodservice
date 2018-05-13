package com.lichuan.sale.web;
import com.lichuan.sale.core.Result;
import com.lichuan.sale.core.ResultGenerator;
import com.lichuan.sale.model.Vercode;
import com.lichuan.sale.service.VercodeService;
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
@RequestMapping("/vercode")
public class VercodeController {
    @Resource
    private VercodeService vercodeService;

    @PostMapping("/add")
    public Result add(Vercode vercode) {
        vercodeService.save(vercode);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        vercodeService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Vercode vercode) {
        vercodeService.update(vercode);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Vercode vercode = vercodeService.findById(id);
        return ResultGenerator.genSuccessResult(vercode);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Vercode> list = vercodeService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
