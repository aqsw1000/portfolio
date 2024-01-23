package portfolio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class webpage {
	@Autowired
	BasicDataSource dataSource;

	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	int result = 0;
	String script = "";
	String msg = null;
	String resul = null;
	int count = 0;

	@PostMapping("/add_master.do")
	public String test(@RequestParam String aid, @RequestParam String apw, @RequestParam String aname,
			@RequestParam String aemail, @RequestParam String atel, @RequestParam String apart,
			@RequestParam String aposition, Model m) {
		security sc = new security(apw);
		String pass = sc.md5_se();

		try {
			this.con = dataSource.getConnection();
			String sql = "insert into admin values ('0',?,?,?,?,?,?,?,'0','N',now())";
			this.ps = this.con.prepareStatement(sql);
			this.ps.setString(1, aid);
			this.ps.setString(2, pass);
			this.ps.setString(3, aname);
			this.ps.setString(4, aemail);
			this.ps.setString(5, atel);
			this.ps.setString(6, apart);
			this.ps.setString(7, aposition);
			this.result = this.ps.executeUpdate();
			if (this.result > 0) {
				this.script = "<script>alert('회원가입이 완료되었습니다.');location.href='./index.jsp';</script>";
			} else {
				this.script = "<script>alert('시스템 오류로 인해 회원가입이 되지 않았습니다.');location.href='./index.jsp';</script>";
			}
			this.ps.close();
			this.con.close();

		} catch (Exception e) {
			System.out.println("오류발생");
		}
		m.addAttribute("script", this.script);

		return "scriptpage";
	}

	@GetMapping("/idcheck.do")
	public String idcheck(@RequestParam String aid, Model m) {
		try {
			this.con = dataSource.getConnection();
			String sql = "select count(*) as ctn from admin where aid=?";
			this.ps = this.con.prepareStatement(sql);
			this.ps.setString(1, aid);
			this.rs = this.ps.executeQuery();
			while (this.rs.next()) {
				if (this.rs.getString("ctn").equals("0")) {
					this.msg = "0";
				} else {
					this.msg = "1";
				}
			}
			this.rs.close();
			this.ps.close();
			this.con.close();
		} catch (Exception e) {
			System.out.println("오류입니다");
		}
		m.addAttribute("msg", this.msg);
		return null;
	}

	@PostMapping("/loginok.do")
	public String loginok(@RequestParam String aid, @RequestParam String apw, Model m, HttpServletRequest req) {
	    try {
	        security sc = new security(apw);
	        String pass = sc.md5_se();

	        this.con = dataSource.getConnection();
	        String sql = "SELECT * FROM admin WHERE aid=? AND apw=?";
	        this.ps = this.con.prepareStatement(sql);
	        this.ps.setString(1, aid);
	        this.ps.setString(2, pass);
	        this.rs = this.ps.executeQuery();

	        if (this.rs.next()) {
	            int num = this.rs.getInt("acount");
	            String status = this.rs.getString("ause");

	            if (num > 5) {
	                this.script = "<script>alert('로그인 5회 실패로 차단되었습니다.');history.go(-1);</script>";
	            } else if ("N".equals(status)) {
	                this.script = "<script>alert('미승인 아이디 입니다.');history.go(-1);</script>";
	            } else {
	                HttpSession session = req.getSession();
	                session.setAttribute("aid", aid);
	                session.setAttribute("aname", this.rs.getString("aname"));
	                session.setAttribute("ause", status);
	                this.script = "<script>alert('정상적으로 로그인 되셨습니다.');location.href='./admin_list.do';</script>";
	            }
	        } else {
	            this.script = "<script>alert('아이디나 비밀번호가 틀렸습니다. 다시 로그인 해주세요');location.href='./index.jsp';</script>";
	            String updateSql = "UPDATE admin SET acount=acount+1 WHERE aid=?";
	            this.ps = this.con.prepareStatement(updateSql);
	            this.ps.setString(1, aid);
	            int succ = this.ps.executeUpdate();

	            if (succ > 0) {
	            } else {
	            }

	            this.count++;
	        }

	        this.rs.close();
	        this.ps.close();
	        this.con.close();
	    } catch (Exception e) {
	        System.out.println("오류!!!");
	    }

	    m.addAttribute("script", this.script);
	    return "scriptpage";
	}

	@GetMapping("/logout.do")
	public String logout(HttpServletRequest req, Model m) {
		HttpSession session = req.getSession();
		session.removeAttribute("aid");
		session.removeAttribute("aname");
		session.removeAttribute("ause");
		session.invalidate();
		this.script = "<script>alert('정상적으로 로그아웃 되셨습니다.');location.href='./index.jsp';</script>";
		m.addAttribute("script", script);
		return "scriptpage";
	}
	
	@PostMapping("/admin_siteinfo.do")
	public String admin_siteinfo(@RequestParam String htitle,
			@RequestParam String hademail,@RequestParam String hpoint,
			@RequestParam String hmoney,@RequestParam String hlevel,
			@RequestParam String hname,@RequestParam String hnumber,
			@RequestParam String hleader,@RequestParam String htel,
			@RequestParam String htraffic,@RequestParam String hsub,
			@RequestParam String hpost,@RequestParam String hadd,
			@RequestParam String hhead,@RequestParam String hemail,
			@RequestParam String hbank,@RequestParam String haccount,
			@RequestParam String hcard,@RequestParam String htelbank,
			@RequestParam String hcertificate,@RequestParam String hminpoint,
			@RequestParam String hmaxoint,@RequestParam String hreceipt,Model m) {
		try {
			this.con = dataSource.getConnection();
			String sql = "update homepage set htitle=? , hademail=? , hpoint=? , hmoney=? , hlevel=? , hname=? , hnumber=? , hleader=? , htel=? , htraffic=? , hsub=? , hpost=? , hadd=? , hhead=? , hemail=? , hbank=? , haccount=? , hcard=? , htelbank=? , hcertificate=? , hminpoint=? , hmaxoint=? , hreceipt=? where hidx='1'";
			this.ps = this.con.prepareStatement(sql);
			this.ps.setString(1, htitle);
			this.ps.setString(2, hademail);
			this.ps.setString(3, hpoint);
			this.ps.setString(4, hmoney);
			this.ps.setString(5, hlevel);
			this.ps.setString(6, hname);
			this.ps.setString(7, hnumber);
			this.ps.setString(8, hleader);
			this.ps.setString(9, htel);
			this.ps.setString(10, htraffic);
			this.ps.setString(11, hsub);
			this.ps.setString(12, hpost);
			this.ps.setString(13, hadd);
			this.ps.setString(14, hhead);
			this.ps.setString(15, hemail);
			this.ps.setString(16, hbank);
			this.ps.setString(17, haccount);
			this.ps.setString(18, hcard);
			this.ps.setString(19, htelbank);
			this.ps.setString(20, hcertificate);
			this.ps.setString(21, hminpoint);
			this.ps.setString(22, hmaxoint);
			this.ps.setString(23, hreceipt);
	
			this.result = this.ps.executeUpdate();
			if(this.result > 0){
				this.script = "<script>alert('정상적으로 저장 되었습니다.');location.href='./admin_siteinfo.do';</script>";
			} else {
				this.script = "<script>alert('시스템 오류 발생으로 저장이 실패 하였습니다.');location.href='./admin_siteinfo.do';</script>";
			}
			this.ps.close();
			this.con.close();
		} catch (Exception e) {
			System.out.println("오류");
		}
		m.addAttribute("script", script);
		return null;
	}
}
