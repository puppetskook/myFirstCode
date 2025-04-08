package com.ysw.controller;

import com.alibaba.druid.sql.ast.SQLObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sun.media.sound.ModelOscillator;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.ysw.entity.User;
import com.ysw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tk.mybatis.spring.annotation.MapperScan;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@MapperScan("com.ysw.dao")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/main")
    public String tomain() {
        return "main";
    }

    @GetMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(value = "name") String name,
                        @RequestParam(value = "password") String password,
                        Map<String, Object> maps, HttpSession session) {
        User user = userService.login(name, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/main";
        }else {
            maps.put("msg","用户名或没密码不正确");
            return "login";
        }
    }

    @GetMapping("/getUserList")
    public String getUserList(Model model) {
        List<User> userList = userService.getUserList();
        if (userList != null) {
            model.addAttribute("userList", userList);
            return "ulist";
        }else {
            return null;
        }
    }

    @GetMapping("/quit")
    public String logout(HttpSession session) {
        // 清除 session 中的所有属性
        session.invalidate();
        // 重定向到登录页面或首页，这里假设是登录页面
        return "redirect:/toLogin";
    }

    @RequestMapping("/deletUser/{id}")
    public String deleteUser(@PathVariable("id") int id,Model model) throws Exception {
        int num = userService.deleteUser(id);
        userService.resetAutoIncrement();
        if (num > 0) {
            model.addAttribute("msg","删除成功！！！");
        }else {
            model.addAttribute("msg","删除失败！！！");

        }

        return "forward:/page/1/3";
    }

    @GetMapping("/selectUserById/{id}")
    public String selectUserById(@PathVariable(value = "id") int id,Model model) {
        User user = userService.selectUserById(id);
        if (user != null){
            model.addAttribute("user", user);
            return "showUserList";
        }else{
            return null;
        }
    }

    //未完成
    @GetMapping("/showUpdateUser/{id}")
    public String showUpdateUser(@PathVariable(value = "id") int id,Model model) {
        User user = userService.selectUserById(id);
        if (user != null){
            model.addAttribute("user", user);
            return "updateUser";
        }else{
            return null;
        }
    }


    @RequestMapping("/updateUser")
    public String updateUser(User user,RedirectAttributes redirectAttributes){
        int a = userService.updateUser(user);

        if (a > 0){
            redirectAttributes.addFlashAttribute("msg","修改成功!!!");
        }else {
            redirectAttributes.addFlashAttribute("msg","修改失败!!!");
        }
        Long num = user.getId();
        if (num%3>0){return "redirect:/page/"+(num/3+1)+"/3";
        }else {return "redirect:/page/"+(num/3)+"/3";}

    }


    @GetMapping("/showInsertUser")
    public String showInsertUser(){
        return "insertUser";
    }

    @PostMapping("/insertUser")
    public String insertUser(User user, RedirectAttributes redirectAttributes){
        int num = userService.insertUser(user);
        if (num > 0){
            redirectAttributes.addFlashAttribute("msg","增加成功！！！");
        }else{
            redirectAttributes.addFlashAttribute("msg","增加失败！！！");
        }
        Long num2 = user.getId();
        if (num%3>0){return "redirect:/page/"+(num2/3+1)+"/3";
        }else {return "redirect:/page/"+(num2/3)+"/3";}
    }

    @GetMapping("/page/{pageNum}/{pageSize}")
    public String getUserForPage(@PathVariable(value = "pageNum") Integer pageNum,
                                 @PathVariable(value = "pageSize") Integer pageSize,
                                 Model model) {
        PageInfo<User> pageInfo = userService.getPage(pageNum,pageSize);
        if (pageInfo != null) {
            model.addAttribute("pageInfo", pageInfo);
        }else {
            model.addAttribute("pageInfo", null);
        }
        return "ulist";
    }



    //清除批量删除的id
    @GetMapping("/clearMoreUserId")
    public String clearMoreUserId(HttpSession session) {
        session.setAttribute("myList", null);
        return "forward:/page/1/3";
    }
    //接收批量删除的id
    @GetMapping("/addMoreIdToDelete/{id}")
    public String addMoreIdToDelete(@PathVariable(value = "id") Integer selectId,HttpSession session,RedirectAttributes redirectAttributes){
        int num = selectId;
        List<Integer> myList = (List<Integer>) session.getAttribute("myList");
        if (myList == null) {
            // 如果列表不存在，创建一个新的列表
            myList = new ArrayList<>();
        }
        if (!myList.contains(selectId)) {
            myList.add(selectId);
        }else {
            redirectAttributes.addFlashAttribute("msg","不能选择相同数据");
        }
        //selectId为被选择的id
        session.setAttribute("myList",myList);
        //返回判断
        if (num%3>0){return "redirect:/page/"+(num/3+1)+"/3";
        }else {return "redirect:/page/"+(num/3)+"/3";}
    }



    //批量删除
    @GetMapping("/deleteMoreUser")
    public String deleteMoreUser(HttpSession session,RedirectAttributes redirectAttributes) throws Exception {
        List<Integer> myList = (List<Integer>) session.getAttribute("myList");
        //判断数组中是否为空，再进行批量删除
        if (myList == null){
            redirectAttributes.addFlashAttribute("msg","没有选择数据");
        }else {
            for (int n:myList){
                userService.deleteUser(n);
            }
            redirectAttributes.addFlashAttribute("msg","删除成功！！！");
        }
        //清除seesion中myList数组中的信息
        session.setAttribute("myList", null);
        userService.resetAutoIncrement();
        return "redirect:/page/1/3";
    }




    //搜索跳转
    @GetMapping("/showFindByNameContaining")
    public String showFindByNameContaining(){
        return "showFindByNameContaining";
    }
    //模糊搜索功能
    @RequestMapping ("/findByNameContaining")
    public String findByNameContaining(@RequestParam(value = "name",required = false) String name,
                                       @RequestParam(value = "sex",required = false) String sex,
                                       @RequestParam(value = "age",required = false) Integer age,
                                       Model model) {
        List<User> users = userService.findByNameContaining(name,sex,age);
        if (users != null) {
            model.addAttribute("users", users);
            model.addAttribute("msg", "搜索成功！！！");
        }else {
            model.addAttribute("users", null);
            model.addAttribute("msg", "没有这个用户！！！");
        }
        return "showFindByNameContaining";
    }



    //注册功能
    @GetMapping("/toRegisterPage")
    public String toRegister(){
        return "register";
    }
    @PostMapping("/toRegister")
    public String toRegister(User user,Model model) {
        User UserName =  userService.findByName(user.getName());
        if(UserName == null){
            int i = userService.insertUser(user);
            if (i > 0) {
                model.addAttribute("msg","注册成功！！！");
                return "login";
            }else {
                model.addAttribute("msg","注册失败");
                return "register";
            }
        }else {
            model.addAttribute("msg","用户已存在！！！");
            return "register";
        }
    }


    //滑块验证
    @GetMapping("/index111")
    public String index111(){
        return "index111";
    }



}
