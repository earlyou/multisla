package com.multi.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.multi.biz.CarbuildBiz;
import com.multi.biz.ColorBiz;
import com.multi.biz.GarageBiz;
import com.multi.biz.InteriorBiz;
import com.multi.biz.ModelBiz;
import com.multi.biz.UsersBiz;
import com.multi.biz.WheelBiz;
import com.multi.vo.CarbuildVO;
import com.multi.vo.ColorVO;
import com.multi.vo.GarageVO;
import com.multi.vo.InteriorVO;
import com.multi.vo.ModelVO;
import com.multi.vo.UsersVO;
import com.multi.vo.WheelVO;

// 신승욱
@Controller
public class MainController3 {
	
	@Autowired
	CarbuildBiz carbuildbiz;
	
	@Autowired
	GarageBiz garagebiz;
	
	@Autowired
	UsersBiz usersbiz;
	
	@Autowired
	ModelBiz modelbiz;
	
	@Autowired
	ColorBiz colorbiz;
	
	@Autowired
	WheelBiz wheelbiz;
	
	@Autowired
	InteriorBiz interiorbiz;
	
	// always return garage list for index.html
	@ModelAttribute("cartlist")
	public List<GarageVO> cartmenu() {
		List<GarageVO> cartlist = null;
		try {
			cartlist = garagebiz.getcart();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cartlist;
	}
	
	// go to model S detail page
	@RequestMapping("/models")
	public String models(Model m) {
		m.addAttribute("center", "detail/modelSdetail");
		return "index";
	}
	
	// build model Y
	@RequestMapping("/buildmodely")
	public String buildmodely(Model m, HttpSession session) {
		ModelVO model = null;
		List<ColorVO> color = null;
		List<WheelVO> wheel = null;
		List<InteriorVO> inter = null;
		try {
			model = modelbiz.get(101);
			color = colorbiz.get();
			wheel = wheelbiz.get();
			inter = interiorbiz.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		m.addAttribute("model", model);
		m.addAttribute("color", color);
		m.addAttribute("wheel", wheel);
		m.addAttribute("interior", inter);
		m.addAttribute("session", session.getAttribute("loginusers"));
		m.addAttribute("center", "carbuild/modelY");
		return "index";
	}
	
	// build model S
	@RequestMapping("/buildmodels")
	public String carbuild(Model m, HttpSession session) {
		ModelVO model = null;
		List<ColorVO> color = null;
		List<WheelVO> wheel = null;
		List<InteriorVO> inter = null;
		try {
			model = modelbiz.get(100);
			color = colorbiz.get();
			wheel = wheelbiz.get();
			inter = interiorbiz.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		m.addAttribute("model", model);
		m.addAttribute("color", color);
		m.addAttribute("wheel", wheel);
		m.addAttribute("interior", inter);
		m.addAttribute("session", session.getAttribute("loginusers"));
		m.addAttribute("center", "carbuild/modelS");
		return "index";
	}
	
	// build model X
	@RequestMapping("/buildmodelx")
	public String buildmodelx(Model m, HttpSession session) {
		ModelVO model = null;
		List<ColorVO> color = null;
		List<WheelVO> wheel = null;
		List<InteriorVO> inter = null;
		try {
			model = modelbiz.get(102);
			color = colorbiz.get();
			wheel = wheelbiz.get();
			inter = interiorbiz.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		m.addAttribute("model", model);
		m.addAttribute("color", color);
		m.addAttribute("wheel", wheel);
		m.addAttribute("interior", inter);
		m.addAttribute("session", session.getAttribute("loginusers"));
		m.addAttribute("center", "carbuild/modelX");
		return "index";
	}
	
	
	// build -> garage
	// login 상태에서 build하면 garage로 이동
	@RequestMapping("/gogarage")
	public String gogarage(Model m, CarbuildVO obj, String uid) {
		int codeno = 0;
		try {
			carbuildbiz.register(obj);				// register obj to carbuild table
			codeno = carbuildbiz.selectlast();		// get PK FROM latest registered carbuild tuple
			GarageVO g = new GarageVO(codeno, uid);	// create new GarageVO object
			garagebiz.register(g);					// register GarageVO object
		} catch (Exception e) {
			e.printStackTrace();
		}
		m.addAttribute("center", "garage/garage");
		return "redirect:/garage?uid="+uid;
	}
	
	// build -> login
	// login하지 않고 build할 때, login으로 이동
	@RequestMapping("/buildlogin")
	public String buildlogin(Model m) {
		m.addAttribute("center", "carbuild/login");
		return "index";
	}
	
	// build -> login -> garage
	// login하지 않고 build할 때, login 성공하면 쿠키 정보를 garage로 register
	@RequestMapping("/logintogarage")
	public String logintogarage(Model m, String uid, String upwd, HttpSession session, HttpServletRequest resq) {
		UsersVO users = null;
		try {
			users = usersbiz.get(uid);
			if (users != null) {
				if (users.getUpwd().equals(upwd)) {
					session.setAttribute("loginusers", users);
					m.addAttribute("loginusers", users);
				} else {
					throw new Exception();
				}
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			m.addAttribute("center", "carbuild/login");
			m.addAttribute("msg", "회원정보를 확인해주세요");
			return "index";
		}
		Cookie[] cookie = resq.getCookies();
		int mid = 0;
		int colid = 0;
		int wid = 0;
		int iid = 0;
		boolean corder = false;
		for (Cookie c : cookie) {	// cookie value를 변수에 담기
			if (c.getName().equals("mid")) {
				mid = Integer.parseInt(c.getValue());
			}
			if (c.getName().equals("colid")) {
				colid = Integer.parseInt(c.getValue());
			}
			if (c.getName().equals("wid")) {
				wid = Integer.parseInt(c.getValue());
			}
			if (c.getName().equals("iid")) {
				iid = Integer.parseInt(c.getValue());
			}
			if (c.getName().equals("corder")) {
				corder = Boolean.parseBoolean(c.getValue());
			}
		}
		try {	// cookie값을 담은 변수로 carbuild를 register하고 그 후에 garage에 register
			carbuildbiz.register(new CarbuildVO(mid, colid, wid, iid, corder));
			int codeno = carbuildbiz.selectlast();
			garagebiz.register(new GarageVO(codeno, users.getUid()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		m.addAttribute("center", "garage/garage");
		return "redirect:/garage?uid="+users.getUid();
	}
	
	// build -> login -> register
	// build 후 login에서 계정이 없을 때 register로 이동
	@RequestMapping("/buildregister")
	public String buildregister(Model m) {
		m.addAttribute("center", "carbuild/register");
		return "index";
	}
	
	// build -> login -> register -> garage
	// 회원가입을 하고 자동 로그인 후 garage로 이동
	@RequestMapping("/build_register")
	public String build_register(Model m, UsersVO obj, HttpSession session, HttpServletRequest resq) {
		UsersVO users = null;
		try {
			usersbiz.register(obj);
			users = usersbiz.get(obj.getUid());
			session.setAttribute("loginusers", users);	// 자동 로그인
			m.addAttribute("loginusers", users);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Cookie[] cookie = resq.getCookies();	// servlet에서 쿠키 요청 후 cookie 배열에 담기
		int mid = 0;
		int colid = 0;
		int wid = 0;
		int iid = 0;
		boolean corder = false;
		for (Cookie c : cookie) {		// 각각의 변수에 쿠키의 value 넣기
			if (c.getName().equals("mid")) {
				mid = Integer.parseInt(c.getValue());
			}
			if (c.getName().equals("colid")) {
				colid = Integer.parseInt(c.getValue());
			}
			if (c.getName().equals("wid")) {
				wid = Integer.parseInt(c.getValue());
			}
			if (c.getName().equals("iid")) {
				iid = Integer.parseInt(c.getValue());
			}
			if (c.getName().equals("corder")) {
				corder = Boolean.parseBoolean(c.getValue());
			}
		}
		try {	// 받아 온 쿠키 value를 기반으로 carbuild에 register, garage에도 register
			carbuildbiz.register(new CarbuildVO(mid, colid, wid, iid, corder));
			int codeno = carbuildbiz.selectlast();
			garagebiz.register(new GarageVO(codeno, users.getUid()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		m.addAttribute("center", "garage/garage");
		return "redirect:/garage?uid="+users.getUid();
	}
	
	// garage detail -> modify build
	// 차고 상세 조회에서 수정하기 누르면 수정 페이지로 이동
	@RequestMapping("/modifygarage")
	public String modifygarage(Model mo, int gid) {
		GarageVO g = null;
		CarbuildVO car = null;
		ModelVO m = null;
		ColorVO c = null;
		WheelVO w = null;
		InteriorVO i = null;
		List<ColorVO> color = null;
		List<WheelVO> wheel = null;
		List<InteriorVO> inter = null;
		try {
			g = garagebiz.get(gid);
			car = carbuildbiz.get(g.getCodeno());
			m = modelbiz.get(car.getMid());
			c = colorbiz.get(car.getColid());
			w = wheelbiz.get(car.getWid());
			i = interiorbiz.get(car.getIid());
			color = colorbiz.get();
			wheel = wheelbiz.get();
			inter = interiorbiz.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mo.addAttribute("g", g);
		mo.addAttribute("car", car);
		mo.addAttribute("m", m);
		mo.addAttribute("c",c);
		mo.addAttribute("w", w);
		mo.addAttribute("i", i);
		mo.addAttribute("color", color);
		mo.addAttribute("wheel", wheel);
		mo.addAttribute("interior", inter);
		mo.addAttribute("center", "garage/modify");
		return "index";
	}
	
	// modify build -> garage
	// 수정페이지에서 modify 하고 garage로 이동
	@RequestMapping("/modifybuild")
	public String modifybuild(Model m, CarbuildVO obj, String uid) {
		try {
			carbuildbiz.modify(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		m.addAttribute("center", "garage/garage");
		return "redirect:/garage?uid="+uid;
	}
}
