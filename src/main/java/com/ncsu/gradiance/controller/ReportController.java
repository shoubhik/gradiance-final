package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.dao.UserDao;
import com.ncsu.gradiance.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reports")
@SessionAttributes("user")
public class ReportController extends BaseController{


    private UserDao userDao;
    @Autowired
    public ReportController(UserDao userDao){
        this.userDao = userDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showForm(@ModelAttribute("user") User user){
        if(!user.isProf())
            throw new AuthorizationException();
        return "reports";

    }

    @RequestMapping(method = RequestMethod.POST)
    public String showResult(
            @ModelAttribute("user") User user,
            BindingResult result, Model model, HttpServletRequest request) {
        String query = request.getParameter("report.query");
        if (!StringUtils.isBlank(query)) {
            List<Map<String, Object>>  outs = this.userDao.makeGeneralQuery(query);
            List<List<String>> cols = new ArrayList<>();
            // prepare header
            if(outs.size() > 0){
                Map<String, Object> headerMap = outs.get(0);
                List<String> row = new ArrayList<>();
                for(String header : headerMap.keySet())
                    row.add(header);
                cols.add(row);
            }
            for(Map<String, Object> out : outs){
                List<String> row = new ArrayList<>();
                for(Map.Entry<String, Object> entry : out.entrySet()){
                    row.add(entry.getValue().toString());
                }
                cols.add(row);
            }
            model.addAttribute("showQuery", true);

                model.addAttribute("data", cols);
            }
        return "reports";
    }
}
