package com.wt.common;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2017/8/31.
 */
public class BaseAction {

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected HttpSession session;



    @ModelAttribute

    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){

        this.request = request;

        this.response = response;

        this.session = request.getSession();

    }
    /**
     * respond by JSON
     *
     * @param object
     * @throws IOException
     */
    public void writeJson(Object object) {
        try {
            String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
			/*
			 * String json = null; try { json =
			 * JacksonJsonUtil.beanToJson(object); } catch (Exception e) {
			 * e.printStackTrace(); }
			 */
            this.response.setContentType("text/html;charset=utf-8");
            this.response.getWriter().write(json);
            this.response.getWriter().flush();
            this.response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // revise json, make icon display
    public void writeJson_icon(Object object) {
        try {
            String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
			/*
			 * String json = null; try { json =
			 * JacksonJsonUtil.beanToJson(object); } catch (Exception e) {
			 * e.printStackTrace(); }
			 */
            String json_icon = json.replace("iconcls", "iconCls");
            this.response.setContentType("text/html;charset=utf-8");
            this.response.getWriter().write(json_icon);
            this.response.getWriter().flush();
            this.response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 简单的ajax返回字符串
     *
     * @param backStr
     */
    public void ajaxBack(String backStr) {
        this.response.setContentType("text/plain; charset=UTF-8");
        this.response.setHeader("Content-Disposition", "inline");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(backStr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
}
