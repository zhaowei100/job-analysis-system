package com.swpu.jobanalysissystem.controller;

import com.alibaba.fastjson.JSONObject;
import com.swpu.jobanalysissystem.dao.UserMapper;
import com.swpu.jobanalysissystem.pojo.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class RegisterController {
  @Autowired
  private UserMapper userMapper;
  @RequestMapping(value = { "/register.html"})
  public String RegisterLogin(){ return "register"; }

  @RequestMapping(value = "/register", method = POST)
  public String registerAction(@Valid Login login, Model model, Errors errors){
    if(errors.hasErrors()){
      //收到表单信息错误时的操作
      String str = "请不要做非法操作！！";
      model.addAttribute("error", str);
      return "register?error";
    }

    //数据库是否已经存在信息
    Integer isRegistered = userMapper.isRegistered(login);
    if(isRegistered != null){
      //已经存在账号信息
      return "redirect:/register.html?error";
    }else{
      //判断注册成功否

      int flag = userMapper.addUserRegister(login);
      if(flag > 0){
        //注册成功
        System.out.println(login.getEmail());

      }else{
        //注册失败
        return "redirect:/register.html?error";
      }

    }
    return "redirect:/";
  }

  @ResponseBody
  @RequestMapping("/checkUserName.action")
  public String checkUserName(@RequestParam String userName){
    //数据库是否已经存在信息
    JSONObject result = new JSONObject();
    Integer isRegistered = userMapper.isRegisteredByName(userName);
    if(isRegistered != null) {
      result.put("success","error");
    }else{
      result.put("success","success");
    }
    return result.toJSONString();
  }
  @ResponseBody
  @RequestMapping("/checkUserEmail.action")
  public String checkUserEmail(@RequestParam String email){
    //数据库是否已经存在 email

    JSONObject result = new JSONObject();
    Integer isRegistered = userMapper.isRegisteredByEmail(email);
    if(isRegistered != null) {
      result.put("success","error");
    }else{
      result.put("success","success");
    }
    return result.toJSONString();
  }
}
