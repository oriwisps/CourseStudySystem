package com.zjj.coursestudy.controller;

import com.zjj.coursestudy.entity.Discussion;
import com.zjj.coursestudy.entity.User;
import com.zjj.coursestudy.model.RespBean;
import com.zjj.coursestudy.service.DiscussionService;
import com.zjj.coursestudy.service.UserService;
import com.zjj.coursestudy.utils.JwtUtil;
import com.zjj.coursestudy.vo.DiscussionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/discussion")
public class DiscussionController {

    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private UserService userService;

    /**
     * 查询用户发送的消息
     * @param token
     * @return
     */
    @GetMapping("/sender")
    public RespBean getDiscussionBySender(@RequestHeader(value = "token") String token){
        RespBean respBean = RespBean.requestError("查询失败");
        String userName = JwtUtil.getUserName(token);

        List<DiscussionVo> discussionVoList = new ArrayList<>();
        User user = userService.getUserByName(userName);
        if(user != null){
            List<Discussion> discussionList = discussionService.findBySender(user);
            for (Discussion d: discussionList) {
                discussionVoList.add(new DiscussionVo(d));
            }
            respBean = RespBean.ok("查询成功", discussionVoList);
            return respBean;
        }
        return respBean;
    }

    /**
     * 查询用户接收的消息
     * @param token
     * @return
     */
    @GetMapping("/receiver")
    public RespBean getDiscussionByReceiver(@RequestHeader(value = "token") String token){
        RespBean respBean = RespBean.requestError("查询失败");
        String userName = JwtUtil.getUserName(token);

        List<DiscussionVo> discussionVoList = new ArrayList<>();
        User user = userService.getUserByName(userName);
        if(user != null){
            List<Discussion> discussionList = discussionService.findByReceiver(user);
            for (Discussion d: discussionList) {
                discussionVoList.add(new DiscussionVo(d));
            }
            respBean = RespBean.ok("查询成功", discussionVoList);
            return respBean;
        }
        return respBean;
    }

    /**
     * 发送消息
     * @param receiverID
     * @param content
     * @param token
     * @return
     */
    @PostMapping("/send")
    public RespBean sendDiscussion(@RequestParam(value = "receiverID") String receiverID,
                                   @RequestParam(value = "content") String content,
                                   @RequestHeader(value = "token") String token){
        RespBean respBean = RespBean.requestError("发送失败");
        String userName = JwtUtil.getUserName(token);
        User sender = userService.getUserByName(userName);
        User receiver = userService.getUserByID(receiverID);
        if(sender != null && receiver != null){
            Discussion discussion = new Discussion();
            discussion.setContent(content);
            discussion.setSender(sender);
            discussion.setReceiver(receiver);
            discussion.setTime(new Date(System.currentTimeMillis()));
            discussion = discussionService.saveDiscussion(discussion);
            if(discussion != null){
                respBean = RespBean.ok("发送成功");
                return respBean;
            }
        }
        return respBean;
    }
}
