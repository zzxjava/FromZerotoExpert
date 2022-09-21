package com.zzx.FromZerotoExpert.controller;

import com.zzx.FromZerotoExpert.common.Result;
import com.zzx.FromZerotoExpert.model.dao.UserMapper;
import com.zzx.FromZerotoExpert.model.pojo.User;
import com.zzx.FromZerotoExpert.service.UserService;
import com.zzx.FromZerotoExpert.utils.MD5Utils;
import com.zzx.FromZerotoExpert.utils.PasswordValidator;
import com.zzx.FromZerotoExpert.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return " 嗨，欢迎您来到 from zero to expert.";
    }


    /**
     * 让网站拥有短暂记忆
     * cookie:
     */
    @GetMapping("/FromZerotoExpert")
    @ResponseBody
    public String Test(@CookieValue(value = "testcookie",defaultValue = "false")String visit, HttpServletResponse response){
        if(visit.equals("false")){
            //用户未登录
            Cookie cookie = new Cookie("testcookie", "true");
            //设置cookie的有效时间
            cookie.setMaxAge(24*60*60);
            response.addCookie(cookie);
            return "你好，欢迎您来到 from zero to expert.";
        }
        return "你好，欢迎您再次来到 from zero to expert.";
    }


    /**
     *
     * @param request:请求
     * @param response:响应
     * @throws IOException
     */
    @GetMapping("/FromZerotoExpert2")
    @ResponseBody
    public void test(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        //获取所有cookie
        Cookie[] cookies = request.getCookies();
        boolean flag = false;
        //遍历cookie数组
        if(cookies != null && cookies.length > 0){
            for (Cookie cookie:cookies) {
                //获取cookie名称
                String name = cookie.getName();
                //判断是不是最后一次登录的信息
                if("lastTime".equals(name)){
                    //如果相等，说明已经登录了
                    flag = true;//表示有lasttime的cookie
                    //设置cookie的value
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");//这里不能有空格
                    String str_date = sdf.format(date);
                    //设置cookie的value
                    cookie.setValue(str_date);
                    //设置cookie存活时间
                    cookie.setMaxAge(24*60*60);
                    response.addCookie(cookie);

                    //获取cookie时间,响应数据
                    String value = cookie.getValue();
                    response.getWriter().write("嗨，欢迎您再次来到from zero to expert."+value);
                }
            }
        }
        if(cookies == null || cookies.length == 0|| flag == false){
            //说明是第一次访问
            //设置cookie的value
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
            String str_date = sdf.format(date);
            Cookie cookie = new Cookie("lastTime",str_date);
            //设置cookie存活时间
            cookie.setMaxAge(24*60*60);
            response.addCookie(cookie);
            response.getWriter().write("嗨，欢迎您来到from zero to expert.");
        }
    }


    @GetMapping("/")
    public String helloYou(){
        return "login";     // login是页面
    }

    @GetMapping("/registerShow")
    public String registerShow(){
        return "register";
    }

    /**
     * 注册接口
     * Post请求只能用@RequestBody注解，传入json数据，不能用@RequestParam进行传
     * 1、多用户同时注册，防止用户名重复的
     * @param
     * @param
     * @return
     */
    @PostMapping("/register")
    @ResponseBody
    public Result<User> register(@RequestBody User user){
        User result = userMapper.selectByName(user.getUsername());
        if(result != null){
            //说明数据库中已经创建了该用户
            return Result.error("10", "用户名已注册");
        }
        //对注册的名字进行敏感词过滤
        String filter = sensitiveFilter.filter(user.getUsername());
        if(!filter.equals(user.getUsername())){
            return Result.error("-5", "用户名含有敏感词");
        }
        //对密码长度，字符，大小写进行要求
        boolean valid = PasswordValidator.isValid(user.getPassword());
        if(valid == true){
            //给密码进行md5加密
            try {
                user.setPassword(MD5Utils.getMD5str(user.getPassword()));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            int count= userMapper.insertSelective(user);//插入数据库
            if(count == 0){
                return Result.error("-3", "注册失败");
            }
            return Result.success("1","注册成功",user);
        }else {
            return Result.error("-4", "密码强度弱，必须包含大小写和数字");
        }

    }


    /**
     * 登录接口设计
     * @param user
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public Result<User> login(@RequestBody User user) {
        //对传入的参数进行非空处理
        if (!checkParam(user)) {
            return Result.error("-1", "缺少必要参数");
        }
        //验证账号（这里只对用户名进行验证）
        User dbUser = userMapper.selectByName(user.getUsername());
        if (dbUser == null) {
            return Result.error("-2", "该账号不存在");
        }

        //验证密码，注册的时候进行加密处理
        try {
            user.setPassword(MD5Utils.getMD5str(user.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //去数据库查询
        User userOld = userMapper.selectByUsernameAndPassword(user);
        if(userOld == null){
            //说明登录失败
            return Result.error("-3", "登录失败");
        }
        return Result.success(dbUser);
    }

    private boolean checkParam(User user) {
        return user.getUsername() != null && user.getPassword() != null;
    }


}
