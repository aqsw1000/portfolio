package portfolio;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mysql.cj.xdevapi.JsonArray;


@Controller
public class webpage2 {
	
	@Resource(name="users")
	private module md;
	
	@RequestMapping("/admin_list.do")
	public String admin_list(Model m) {
		List<dto> data = null;
		data = md.all_user();
		m.addAttribute("data",data);
		return null;
	}
	
	@PostMapping("/admin_notice_write.do")
	public String admin_notice_wirte(@ModelAttribute("dao")notice_dao dao,@RequestParam("nfile2") MultipartFile nfile2,Model m,HttpServletRequest req) {
		String file_url = "";
		String script = "";

		FTPClient ftp = new FTPClient();
		ftp.setControlEncoding("utf-8");
		FTPClientConfig cf = new FTPClientConfig();
		String filename = nfile2.getOriginalFilename();
		String host = "iup.cdn1.cafe24.com";
		String user = "wieo1000";
		String pass = "Whrudgns930624!";
		int port = 21;
		try {
			ftp.configure(cf);
			ftp.connect(host,port);
			if(ftp.login(user, pass)) {
				ftp.setFileType(FTP.BINARY_FILE_TYPE);
				//ftp.setFileType(FTP.ASCII_FILE_TYPE); //txt
				int result = ftp.getReplyCode();
				boolean ok = ftp.storeFile("/www/"+filename, nfile2.getInputStream());
				if(ok == true) {
					System.out.println("정상적으로 업로드 되었습니다.");
					m.addAttribute("imgs", filename);
				} else {
					System.out.println("FTP 경로 오류 발생 되었습니다. ");
				}
			} else {
				System.out.println("FTP 정보가 올바르지 않습니다.");
			}
		} catch (Exception e) {
			System.out.println("FTP 접속 정보 오류 및 접속 사용자 아이디/패스워드 오류");
		} finally {
			try {
				ftp.disconnect();
			} catch (Exception e2) {
				System.out.println("서버 루프로 인하여 접속 종료장애 발생!!");
			}
			
		}
		dao.setNfile(filename);
		int result = md.notice(dao);


		
		if(result > 0) {
			script = "<script>alert('정상적으로 작성이 완료되었습니다.');location.href='./notice_list.do';</script>";
		} else {
			script = "<script>alert('시스템 오류로 작성이 취소됐습니다.');location.href='./notice_list.do';</script>";
		}
		m.addAttribute("script", script);
		return "scriptpage";
	}
	
	@RequestMapping("/notice_list.do")
	public String notice_list(Model m) {
		List<notice_dao> data = null;
		data = md.notice_write();
		m.addAttribute("data",data);
		return null;
	}
	
	@GetMapping("/admin_notice_view.do")
	public String admin_notice_view(@RequestParam(defaultValue = "",required = false) String nidx, Model m,@RequestParam String num) {
		List<String> data = new ArrayList<String>();
		notice_dao dao = md.notice_view(nidx);
		data.add(dao.getNtitle());
		data.add(dao.getNtop());
		data.add(dao.getNwriter());
		data.add(dao.getNfile());
		data.add(dao.getNtext());
		data.add(dao.getNidx());
		data.add(dao.getNumber());
		m.addAttribute("num",num);
		m.addAttribute("data",data);
		return null;
	}
	
	@PostMapping("/ari_newcode.do")
	public String product_new(@ModelAttribute("air")air_dto air,Model m) {
		int result = md.air_newcode(air);
		String script = "";
		
		if(result > 0) {
			if(air.getAiruse().equals("Y")) {
				script = "<script>alert('정상적으로 작성이 완료되었습니다.');location.href='./product_new.jsp';</script>";
			} else {
				script = "<script>alert('정상적으로 작성이 완료되었습니다.');location.href='./product_new.jsp';</script>";
			}
		} else {
			script = "<script>alert('시스템 오류로 작성이 취소됐습니다.');location.href='./product_list.do';</script>";
		}
		m.addAttribute("script", script);
		return "scriptpage";
	}
	/*
	@RequestMapping("/product_list.do")
	public String product_list(Model m) {
		List<plane_dto> data = null;
		data = md.product_list();
		m.addAttribute("data",data);
		return null;
	}
	*/
	@GetMapping("/ari_check.do")
	public String ari_newcode(@RequestParam String aircode,@RequestParam Integer part,Model m) {
		String script = "";
		int msg = 0;
		String result = "";
		if(part == 1) {
			msg = md.airname(aircode,part);
			if(msg == 0) {
				result = "0";
			} else {
				result = "1";
			}
			
		} else if(part == 2) {
			msg = md.airname(aircode,part);
			if(msg == 0) {
				result = "0";
			} else {
				result = "1";
			}
		}
		m.addAttribute("msg",result);
		return null;
	}
	
	@PostMapping("/product_new.do")
	public String product_new(@ModelAttribute("plane")plane_dto plane,Model m) {
		String script = "";
		int result = md.product_new(plane);
		if(result > 0) {
			script = "<script>alert('정상적으로 작성이 완료되었습니다.');location.href='./product_list.do';</script>";
		} else {
			script = "<script>alert('시스템 오류로 작성이 취소됐습니다.');location.href='./product_list.do';</script>";
		}
		m.addAttribute("script",script);
		return "scriptpage";
	}
	
	@RequestMapping("/plane_api.do")
	public String plane_api(Model m) {
		List<air_dto> data = md.plane_api();
		ArrayList<String> data2 = new ArrayList<String>();
		JSONArray ja = new JSONArray();
		for(int a= 0; a < data.size(); a++) {
			if(data.get(a).getAiruse().equals("Y")) {
				JSONObject jo = new JSONObject();
				jo.put("airflight",data.get(a).getAirflight());
				jo.put("aircode",data.get(a).getAircode());
				jo.put("airname",data.get(a).getAirname());
				jo.put("airplane",data.get(a).getAirplane());
				jo.put("aidx",data.get(a).getAidx());
				ja.add(jo);
			}
		}
		JSONObject jo2 = new JSONObject();
		jo2.put("plane", ja);
		String data4 = jo2.toJSONString();
		m.addAttribute("data",data4);
		return null;
	}
	
	@GetMapping("/admin_useck.do")
	public String admin_useck(@RequestParam String aidx,Model m) {
		String script = "";
		String msg = "";
		int result = md.admin_useck(aidx);
		if(result > 0) {
			script = "<script>alert('승인되었습니다.');location.href='./admin_list.do';</script>";
			msg = "1";
		} else {
			script = "<script>alert('시스템오류발생으로 실패하였습니다.');location.href='./admin_list.do';</script>";
			msg = "0";
		}
		m.addAttribute("msg",msg);
		return "scriptpage";
	}
	
	@GetMapping("/admin_useck2.do")
	public String admin_useck2(@RequestParam String aidx,Model m) {
		String script = "";
		String msg = "";
		int result = md.admin_useck2(aidx);
		if(result > 0) {
			script = "<script>alert('미승인되었습니다.');location.href='./admin_list.do';</script>";
			msg = "1";
		} else {
			script = "<script>alert('시스템오류발생으로 실패하였습니다.');location.href='./admin_list.do';</script>";
			msg = "0";
		}
		m.addAttribute("msg",msg);
		return "scriptpage";
	}
	
	@GetMapping("/notice_numdelete.do")
	public String notice_numdelete(@RequestParam String nidx,Model m) {
		return null;
	}
	
	@GetMapping("/notice_delete.do")
	public String notice_delete(@RequestParam String nidx[],Model m,@RequestParam(defaultValue = "",required = false) String url[]) {
		int ea = url.length;
		FTPClient ftp = new FTPClient();
		ftp.setControlEncoding("utf-8");
		FTPClientConfig cf = new FTPClientConfig();
		try {

			String host = "iup.cdn1.cafe24.com";
			String user = "wieo1000";
			String pass = "Whrudgns930624!";
			int port = 21;
			ftp.configure(cf);
			ftp.connect(host,port);
			if(ftp.login(user, pass)) {
				for(int a = 0; a < ea; a++) {
					boolean delok = ftp.deleteFile("/www/"+url[a]);
					if(delok == true) {
						System.out.println("정상적으로 파일이 삭제되었습니다.");
					} else {
						System.out.println("경로 또는 파일문제로 인하여 삭제되지않았습니다.");
					}
				}
			} else {
				System.out.println("FTP 접속 오류 발생!!");
			}
		} catch (Exception e) {
			System.out.println("FTP 접속 정보 오류");
		} finally {
			try {
				ftp.disconnect();
			} catch (Exception e2) {
				System.out.println("CDN 서버");
			}
		}
		
		int data = md.notice_delete(nidx);
		String msg = "";
		if(data > 0) {
			msg = "1";
		} else {
			msg = "0";
		}
		m.addAttribute("msg",msg);
		return "scriptpage";
	}
	
	@RequestMapping("/product_list.do")
	public String product_list(Model m,@RequestParam(defaultValue = "", required = false) String search,@RequestParam(required = false) String part) {
		List<air_view> data = null;
		String search2 = search.replace(" ", "");
		if(search.equals("") || part.equals("")) {
			data = md.product_list();
		} else {
			m.addAttribute("username",search2);
			m.addAttribute("part",part);
			if(part.equals("1")) {
				data = md.search_plane(search2,1);
			} else if(part.equals("2")) {
				data = md.search_plane(search2,2);
			}
		}
		m.addAttribute("data",data);
		return null;
	}
	
	@RequestMapping("/admin_siteinfook.do")
	public String admin_siteinfo(Model m) {
		List<hp_dto> data = new ArrayList<hp_dto>();
		data = md.admin_siteinfo();
		ArrayList<String> data2 = new ArrayList<String>();
		JSONArray ja = new JSONArray();
		for(int a= 0; a < data.size(); a++) {
			JSONObject jo = new JSONObject();
			jo.put("htitle",data.get(a).getHtitle());
			jo.put("hademail",data.get(a).getHademail());
			jo.put("hpoint",data.get(a).getHpoint());
			jo.put("hmoney",data.get(a).getHmoney());
			jo.put("hlevel",data.get(a).getHlevel());
			jo.put("hname",data.get(a).getHname());
			jo.put("hnumber",data.get(a).getHnumber());
			jo.put("hleader",data.get(a).getHleader());
			jo.put("htel",data.get(a).getHtel());
			jo.put("htraffic",data.get(a).getHtraffic());
			jo.put("hsub",data.get(a).getHsub());
			jo.put("hpost",data.get(a).getHpost());
			jo.put("hadd",data.get(a).getHadd());
			jo.put("hhead",data.get(a).getHhead());
			jo.put("hemail",data.get(a).getHemail());
			jo.put("hbank",data.get(a).getHbank());
			jo.put("haccount",data.get(a).getHaccount());
			jo.put("hcard",data.get(a).getHcard());
			jo.put("htelbank",data.get(a).getHtelbank());
			jo.put("hcertificate",data.get(a).getHcertificate());
			jo.put("hminpoint",data.get(a).getHminpoint());
			jo.put("hmaxoint",data.get(a).getHmaxoint());
			jo.put("hreceipt",data.get(a).getHreceipt());
			ja.add(jo);
		}
		JSONObject jo2 = new JSONObject();
		jo2.put("siteinfo", ja);
		String data4 = jo2.toJSONString();
		m.addAttribute("data",data4);
		return null;
	}
	
	@GetMapping("/admin_notice_modify.do")
	public String admin_notice_modify(@RequestParam String nidx,Model m) {
		List<String> data = new ArrayList<String>();
		notice_dao dao = md.notice_view(nidx);
		data.add(dao.getNtitle());
		data.add(dao.getNtop());
		data.add(dao.getNwriter());
		data.add(dao.getNfile());
		data.add(dao.getNtext());
		data.add(dao.getNidx());
		m.addAttribute("data",data);
		return null;
	}
	
	@PostMapping("/notice_modify.do")
	public String notice_modify(notice_dao dao,Model m,HttpServletRequest req,@RequestParam("nfile2") MultipartFile nfile2,@RequestParam String url) {
		String file_url = "";
		String script = "";
		
		FTPClient ftp2 = new FTPClient();
		ftp2.setControlEncoding("utf-8");
		FTPClientConfig cf2 = new FTPClientConfig();
		try {

			String host = "iup.cdn1.cafe24.com";
			String user = "wieo1000";
			String pass = "Whrudgns930624!";
			int port = 21;
			ftp2.configure(cf2);
			ftp2.connect(host,port);
			if(ftp2.login(user, pass)) {
					boolean delok = ftp2.deleteFile("/www/"+url);
					if(delok == true) {
						System.out.println("정상적으로 파일이 삭제되었습니다.");
					} else {
						System.out.println("경로 또는 파일문제로 인하여 삭제되지않았습니다.");
					}
			} else {
				System.out.println("FTP 접속 오류 발생!!");
			}
		} catch (Exception e) {
			System.out.println("FTP 접속 정보 오류");
		} finally {
			try {
				ftp2.disconnect();
			} catch (Exception e2) {
				System.out.println("CDN 서버");
			}
		}
		
		FTPClient ftp = new FTPClient();
		ftp.setControlEncoding("utf-8");
		FTPClientConfig cf = new FTPClientConfig();
		String filename = nfile2.getOriginalFilename();
		String host = "iup.cdn1.cafe24.com";
		String user = "wieo1000";
		String pass = "Whrudgns930624!";
		int port = 21;
		try {
			ftp.configure(cf);
			ftp.connect(host,port);
			if(ftp.login(user, pass)) {
				ftp.setFileType(FTP.BINARY_FILE_TYPE);
				//ftp.setFileType(FTP.ASCII_FILE_TYPE); //txt
				int result = ftp.getReplyCode();
				boolean ok = ftp.storeFile("/www/"+filename, nfile2.getInputStream());
				if(ok == true) {
					System.out.println("정상적으로 업로드 되었습니다.");
					m.addAttribute("imgs", filename);
				} else {
					System.out.println("FTP 경로 오류 발생 되었습니다. ");
				}
			} else {
				System.out.println("FTP 정보가 올바르지 않습니다.");
			}
		} catch (Exception e) {
			System.out.println("FTP 접속 정보 오류 및 접속 사용자 아이디/패스워드 오류");
		} finally {
			try {
				ftp.disconnect();
			} catch (Exception e2) {
				System.out.println("서버 루프로 인하여 접속 종료장애 발생!!");
			}
			
		}
		dao.setNfile(filename);
		int result = md.notice_modify(dao);
		if(result > 0) {
			script = "<script>alert('수정이 정상적으로 완료되었습니다.');location.href='./notice_list.do';</script>";
		} else {
			script = "<script>alert('시스템 오류로 수정이 실패하였습니다.');history.go(-1);</script>";
		}
		m.addAttribute("script",script);
		return "scriptpage";
	}
	
	@GetMapping("/product_delete.do")
	public String product_delete(@RequestParam String pidx[],Model m) {
		ArrayList<String> aa = new ArrayList<String>();
		int data = md.product_delete(pidx);
		
		return null;
	}
	
	@RequestMapping("/air_list.do")
	public String air_list(Model m,@RequestParam(defaultValue = "", required = false) String search,@RequestParam(required = false) String part) {
		List<air_dto> data = null;
		String search2 = search.replace(" ", "");
		if(search.equals("") || part.equals("")) {
			data = md.plane_api();
		} else {
			m.addAttribute("username",search2);
			m.addAttribute("part",part);
			if(part.equals("1")) {
				data = md.search_plane2(search2,1);
			} else if(part.equals("2")) {
				data = md.search_plane2(search2,2);
			}
		}
		m.addAttribute("data",data);
		return null;
	}
	
	@GetMapping("/air_delete.do")
	public String air_delete(@RequestParam String aidx[],Model m) {
		ArrayList<String> aa = new ArrayList<String>();
		int data = md.aircode_delete(aidx);
		String script = "";
		if(data > 0) {
			script = "ok";
		} else {
			script = "no";
		}
		m.addAttribute("script",script);
		return "scriptpage";
	}
	
	@GetMapping("/air_modifycode.do")
	public String air_modifycode(@RequestParam String aidx,Model m) {
		List<air_dto> data = md.air_codelist(aidx);
		m.addAttribute("data",data);
		return null;
	}
	
	@PostMapping("/aircodemodify.do")
	public String aircodemodify(air_dto dto,Model m) {
		int data = md.aircodemodify(dto);
		String script = "";
		if(data > 0) {
			script = "<script>location.href='./air_list.do';</script>";
		}
		m.addAttribute("script",script);
		return "scriptpage";
	}
	
	@GetMapping("/aircodedelete.do")
	public String aircodedelete(@RequestParam String aidx, Model m) {
		int data = md.aircodedelete(aidx);
		String script = "";
		if(data > 0) {
			script = "<script>alert('정상적으로 삭제가 되었습니다.');location.href='./air_list.do';</script>";
		} else {
			script = "<script>alert('시스템 오류로 인해 삭제가 취소됐습니다.');location.href='./air_list.do';</script>";
		}
		m.addAttribute("script",script);
		return "scriptpage";
	}
	
	@RequestMapping("/admin_seat.do")
	public String admin_seat(Model m,@RequestParam(defaultValue = "", required = false) String search,@RequestParam(required = false) String part) {
		List<air_view> data = null;
		String search2 = search.replace(" ", "");
		if(search.equals("") || part.equals("")) {
			data = new ArrayList<air_view>();
		} else {
			m.addAttribute("username",search2);
			m.addAttribute("part",part);
			if(part.equals("1")) {
				data = md.search_plane3(search2,1);
			} else if(part.equals("2")) {
				data = md.search_plane3(search2,2);
			}
		}
		m.addAttribute("data",data);
		return null;
	}
	
	@PostMapping("/faq_write.do")
	public String faq_write(Model m,faq_dto faq) {
		int data = md.faq_write(faq);
		String script = "";
		if(data > 0) {
			script = "<script>alert('정상적으로 등록이 완료되었습니다.');location.href='./faq_list.do';</script>";
		} else {
			script = "<script>alert('시스템 오류로 인해 등록이 실패하였습니다.');location.href='./faq_list.do';</script>";
		}
		m.addAttribute("script",script);
		return "scriptpage";
	}
	
	@RequestMapping("/faq_list.do")
	public String faq_list(Model m,@RequestParam(defaultValue = "", required = false) String search) {
		List<faq_dto> data = null;
		String search2 = search.replace(" ", "");
		data = md.faq_list();
		if(search.equals("")) {
			data = md.faq_list();
		} else {
			m.addAttribute("username",search2);
			data = md.search_faq(search2);
		}
		m.addAttribute("data",data);
		return null;
	}
	
	@GetMapping("/faq_delete.do")
	public String faq_delete(@RequestParam String fidx, Model m) {
		
		int data = md.faq_delete(fidx);
		String script = "";
		if(data > 0) {
			script = "<script>alert('정상적으로 삭제가 완료 되었습니다.');location.href='./faq_list.do';</script>";
		}
		m.addAttribute("script",script);
		return "scriptpage";
	}
	
	@GetMapping("/faq_modify.do")
	public String faq_modify(@RequestParam String fidx,Model m) {
		List<faq_dto> data = md.faq_modify(fidx);
		m.addAttribute("data",data);
		return null;
	}
	
	@PostMapping("/faq_modifyok.do")
	public String faq_modifyok(faq_dto faq,Model m) {
		int data = md.faq_modifyok(faq);
		String script = "";
		if(data > 0) {
			script = "<script>alert('정상적으로 수정이 완료 되었습니다.');location.href='./faq_list.do';</script>";
		}
		m.addAttribute("script",script);
		return "scriptpage";
	}
	
	@PostMapping("/admin_seatok.do")
	public String admin_seatok(seatlist_dto seat,Model m) {
		int data = md.admin_seatok(seat);
		String script = "";
		if(data > 0) {
			script = "<script>alert('정상적으로 저장이 완료 되었습니다.');location.href='./ticketing_list.do';</script>";
		}
		m.addAttribute("script",script);
		return "scriptpage";
	}
	
	@RequestMapping("/ticketing_list.do")
	public String ticketing_list(Model m,@RequestParam(defaultValue = "", required = false) String search,@RequestParam(required = false) String part) {
		List<ticketlist> data = null;
		
		String search2 = search.replace(" ", "");
		if(search2.equals("") || part.equals("")) {
			data = md.ticketlist();
		} else {
			m.addAttribute("username",search2);
			m.addAttribute("part",part);
			if(part.equals("1")) {
				data = md.search_ticket(search2,1);
			} else if(part.equals("2")) {
				data = md.search_ticket(search2,2);
			}
		}
		m.addAttribute("data",data);
		return null;
	}
	
	@GetMapping("/ticketing_listcancle.do")
	public String ticketing_listcancle(Model m,@RequestParam String tidx) {
		int data = md.ticketing_cancle(tidx);
		String script = "";
		if(data > 0) {
			script = "<script>alert('정상적으로 삭제가 완료 되었습니다.');location.href='./ticketing_list.do';</script>";
		}
		m.addAttribute("script",script);
		return "scriptpage";
	}
	
	@RequestMapping("/air_code2.do")
	public String air_code2(@RequestParam String airplane_code,Model m) {
		List<air_dto> data = md.air_code2(airplane_code);
		JSONArray ja = new JSONArray();
		for(int a= 0; a < data.size(); a++) {
			JSONObject jo = new JSONObject();
			jo.put("airflight",data.get(a).getAirflight());
			jo.put("airname",data.get(a).getAirname());
			ja.add(jo);
		}
		JSONObject jo2 = new JSONObject();
		jo2.put("plane", ja);
		String data4 = jo2.toJSONString();
		m.addAttribute("data",data4);
		return "scriptpage";
	}
	
	@RequestMapping("/air_code3.do")
	public String air_code3(@RequestParam String airname,Model m) {
		List<air_dto> data = md.air_code3(airname);
		JSONArray ja = new JSONArray();
		for(int a= 0; a < data.size(); a++) {
			JSONObject jo = new JSONObject();
			jo.put("airflight",data.get(a).getAirflight());
			jo.put("aircode",data.get(a).getAircode());
			ja.add(jo);
		}
		JSONObject jo2 = new JSONObject();
		jo2.put("plane", ja);
		String data4 = jo2.toJSONString();
		m.addAttribute("data",data4);
		return "scriptpage";
	}
	
	@RequestMapping("/air_code4.do")
	public String air_code4(@RequestParam String aircode,Model m) {
		List<air_dto> data = md.air_code4(aircode);
		JSONArray ja = new JSONArray();
		for(int a= 0; a < data.size(); a++) {
			JSONObject jo = new JSONObject();
			jo.put("airflight",data.get(a).getAirflight());
			jo.put("aidx",data.get(a).getAidx());
			ja.add(jo);
		}
		JSONObject jo2 = new JSONObject();
		jo2.put("plane", ja);
		String data4 = jo2.toJSONString();
		m.addAttribute("data",data4);
		return "scriptpage";
	}
	
	@RequestMapping("/homepage.do")
	public String homepage(Model m) {
		List<hp_dto> data = md.admin_siteinfo();
		JsonArray ja = new JsonArray();
		JSONObject jo = new JSONObject();
		for(int a = 0; a < data.size(); a++) {
			jo.put("1", data.get(a).getHname());
			jo.put("2", data.get(a).getHnumber());
			jo.put("3", data.get(a).getHleader());
			jo.put("4", data.get(a).getHtel());
			jo.put("5", data.get(a).getHtraffic());
			jo.put("6", data.get(a).getHsub());
			jo.put("7", data.get(a).getHpost());
			jo.put("8", data.get(a).getHadd());
			jo.put("9", data.get(a).getHhead());
			jo.put("10", data.get(a).getHemail());
		}
		String data4 = jo.toJSONString();
		m.addAttribute("data4",data4);
		return null;
	}
}
