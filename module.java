package portfolio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository("users")
public class module {

	@Resource(name = "sqlSession")
	private SqlSessionTemplate tm;

	public List<dto> all_user() {
		List<dto> data = new ArrayList<dto>();
		data = tm.selectList("admin_DB.all_admin");
		return data;
	}

	public int notice(notice_dao notice_dao) {
		int result = tm.insert("admin_DB.notice_insert", notice_dao);
		return result;
	}

	public List<notice_dao> notice_write() {
		List<notice_dao> data = new ArrayList<notice_dao>();
		data = tm.selectList("admin_DB.all_notice");
		return data;
	}

	public notice_dao notice_view(String nidx) {
		int data = tm.update("admin_DB.notice_number",nidx);
		notice_dao dao = tm.selectOne("admin_DB.notice_view", nidx);
		return dao;
	}

	public int air_newcode(air_dto dto) {
		int result = tm.insert("admin_DB.air_newcode", dto);
		return result;
	}

	public List<air_view> product_list() {
		List<air_view> data = new ArrayList<air_view>();
		data = tm.selectList("admin_DB.product_list");
		return data;
	}

	public int airname(String aircode, int part) {
		int data = 0;
		if (part == 2) {
			data = tm.selectOne("admin_DB.aircode", aircode);
		}
		return data;
	}

	public int product_new(plane_dto plane) {
		int result = tm.insert("admin_DB.prodcut_new", plane);
		return result;
	}

	public List<air_dto> plane_api() {
		List<air_dto> data = new ArrayList<air_dto>();
		data = tm.selectList("admin_DB.airdao");
		return data;
	}

	public int admin_useck(String aidx) {
		int data = tm.update("admin_DB.adminuse_update", aidx);
		return data;
	}

	public int admin_useck2(String aidx) {
		int data = tm.update("admin_DB.adminuse_update2", aidx);
		return data;
	}

	public int notice_delete(String nidx[]) {
		int ea = nidx.length;
		int w = 0;
		int data = 0;
		while(w < ea) {
			data = tm.delete("admin_DB.notice_delete",nidx[w]);
			w++;
		}
		return data;
	}

	public List<air_view> search_plane(String search, int part) {
		List<air_view> data = new ArrayList<air_view>();

		Map<String, String> m = new HashMap<String, String>();
		m.put("search", search);
		m.put("part", Integer.toString(part));
		data = tm.selectList("admin_DB.search_plane", m);

		return data;
	}
	
	public List<hp_dto> admin_siteinfo() {
		List<hp_dto> dao = new ArrayList<hp_dto>();
		dao = tm.selectList("admin_DB.admin_siteinfo");
		return dao;
	}
	
	public int notice_modify(notice_dao dao) {
		int data = tm.update("admin_DB.notice_modify",dao);
		
		return data;
	}
	
	public int product_delete(String pidx[]) {
		int ea = pidx.length;
		int w = 0;
		int data = 0;
		while(w < ea) {
			data = tm.delete("admin_DB.product_delete",pidx[w]);
			w++;
		}
		
		return data;
	}
	
	public List<air_dto> search_plane2(String search, int part) {
		List<air_dto> data = new ArrayList<air_dto>();

		Map<String, String> m = new HashMap<String, String>();
		m.put("search", search);
		m.put("part", Integer.toString(part));
		data = tm.selectList("admin_DB.search_plane2", m);

		return data;
	}
	
	public List<air_dto> air_codelist(String aidx){
		List<air_dto> data = new ArrayList<air_dto>();
		data = tm.selectList("admin_DB.aircodeview",aidx);
		return data;
	}
	
	public int aircodemodify(air_dto dto) {
		int data = tm.update("admin_DB.aircodemodify",dto);
		return data;
	}
	
	public int aircodedelete(String aidx) {
		int data = tm.delete("admin_DB.aircodedelete",aidx);
		return data;
	}
	
	public int aircode_delete(String aidx[]) {
		int ea = aidx.length;
		int w = 0;
		int data = 0;
		while(w < ea) {
			data = tm.delete("admin_DB.aircodedelete",aidx[w]);
			w++;
		}
		
		return data;
	}
	
	public List<air_view> search_plane3(String search, int part) {
		List<air_view> data = new ArrayList<air_view>();

		Map<String, String> m = new HashMap<String, String>();
		m.put("search", search);
		m.put("part", Integer.toString(part));
		data = tm.selectList("admin_DB.search_plane3", m);

		return data;
	}
	
	public int faq_write(faq_dto faq) {
		int data = tm.insert("admin_DB.faq_write",faq);
		return data;
	}
	
	public List<faq_dto> faq_list(){
		List<faq_dto> data = new ArrayList<faq_dto>();
		data = tm.selectList("admin_DB.faq_list");
		return data;
	}
	
	public int faq_delete(String fidx) {
		int data = tm.delete("admin_DB.faq_delete",fidx);
		return data;
	}
	
	public List<faq_dto> faq_modify(String fidx){
		List<faq_dto> data = new ArrayList<faq_dto>();
		data = tm.selectList("admin_DB.faq_modify",fidx);
		return data;
	}
	
	public int faq_modifyok(faq_dto faq) {
		int data = tm.update("admin_DB.faq_modifyok",faq);
		return data;
	}
	
	public List<faq_dto> search_faq(String search) {
		List<faq_dto> data = new ArrayList<faq_dto>();

		Map<String, String> m = new HashMap<String, String>();
		m.put("search", search);
		data = tm.selectList("admin_DB.search_faq", m);

		return data;
	}
	
	public int admin_seatok(seatlist_dto seat) {
		int data = tm.insert("admin_DB.admin_seatok",seat);
		return data;
	}
	
	public List<ticketlist> ticketlist(){
		List<ticketlist> data = new ArrayList<ticketlist>();
		data = tm.selectList("admin_DB.ticketlist");
		return data;
	}
	
	public int ticketing_cancle(String tidx) {
		int data = tm.delete("admin_DB.ticketing_cancle",tidx);
		return data;
	}
	
	public List<air_dto> air_code2(String airplane) {
		 
		List<air_dto> data = tm.selectList("admin_DB.air_code2",airplane);
		return data;
	}
	
	public List<air_dto> air_code3(String airname) {
		 
		List<air_dto> data = tm.selectList("admin_DB.air_code3",airname);
		return data;
	}
	
	public List<air_dto> air_code4(String aircode) {
		 
		List<air_dto> data = tm.selectList("admin_DB.air_code4",aircode);
		return data;
	}
	
	public List<ticketlist> search_ticket(String search, int part) {
		List<ticketlist> data = new ArrayList<ticketlist>();

		Map<String, String> m = new HashMap<String, String>();
		m.put("search", search);
		m.put("part", Integer.toString(part));
		data = tm.selectList("admin_DB.search_ticket", m);

		return data;
	}
}
