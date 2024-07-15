package cubes.main;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.Hibernate;
import org.hibernate.validator.cfg.defs.SafeHtmlDef.TagDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cubes.main.dao.CategoryDAO;
import cubes.main.dao.EmployeeDAO;
import cubes.main.dao.ProductDAO;
import cubes.main.dao.TagDAO;
import cubes.main.dao.UserDAO;
import cubes.main.entity.Category;
import cubes.main.entity.ChangePassword;
import cubes.main.entity.Employee;
import cubes.main.entity.Product;
import cubes.main.entity.Tag;
import cubes.main.entity.User;

@Controller
@RequestMapping("/administration")
public class AdministrationController {

	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private TagDAO tagDAO;
	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private EmployeeDAO employeeDAO;
	@Autowired
	private UserDAO userDAO;
	
	//PRODUCT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	@RequestMapping({"/product-list", ""})
	public String getProductList(Model model) {
		
		List<Product> list = productDAO.getProductList();
		
		model.addAttribute("productList", list);
		
		return "product-list";
	}

	@RequestMapping("/product-form")
	public String getProductForm(Model model) {
	
		Product product = new Product();
		
		List<Category> categoryList = categoryDAO.getCategoryList();
		List<Tag> tagList = tagDAO.getTagList();
		
		model.addAttribute("product",product);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("tagList", tagList);

		return "product-form";
	}
	
	@RequestMapping("/product-save")
	public String saveProduct(@Valid @ModelAttribute Product product,BindingResult result,Model model) {
		
		if(result.hasErrors()) {
			
			List<Category> categoryList = categoryDAO.getCategoryList();
			List<Tag> tagList = tagDAO.getTagList();
			
			model.addAttribute("categoryList", categoryList);
			model.addAttribute("tagList", tagList);
			
			return "product-form";
		}
		
		Category category = categoryDAO.getCategory(product.getCategory().getId());
		
		List<Integer> ids = new ArrayList<Integer>();
		
		for(Tag tag: product.getTags()) {
			ids.add(Integer.parseInt(tag.getName()));
		}
		
		List<Tag> tags = tagDAO.getTagsById(ids);
		
		product.setCategory(category);
		product.setTags(tags);
		
		productDAO.saveProduct(product);
		
		return "redirect:/administration/product-list";
	}
	
	@RequestMapping("/product-delete")
	public String deleteProduct(@RequestParam int id) {
		
		
		productDAO.deleteProduct(id);
		
		return "redirect:/administration/product-list";
	}
	
	@RequestMapping("/product-form-update")
	public String getProductUpdateForm(@RequestParam int id, Model model) {
		
		Product product = productDAO.getProductWithTag(id);
		
		List<Category> categoryList = categoryDAO.getCategoryList();
		List<Tag> tagList = tagDAO.getTagList();

		
		model.addAttribute("product", product);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("tagList", tagList);

		return "product-form";
	}
	
	//CATEGORY !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	@RequestMapping("/category-list")
	public String getCategoryList(Model model) {
		
		List<Category> list = categoryDAO.getCategoryList();
		
		model.addAttribute("categoryList",list);
		
		return "category-list";
	}
	
	@RequestMapping("/category-form")
	public String getCategoryForm(Model model) {
	
		Category category = new Category();
		
		model.addAttribute("category",category);
		
		return "category-form";
	}
	
	@RequestMapping("/category-form-update")
	public String getCategoryUpdateForm(@RequestParam int id, Model model) {
		
		Category category = categoryDAO.getCategory(id);
		
		model.addAttribute("category", category);
		
		return "category-form";
	}
	
	
	@RequestMapping("/category-save")
	public String saveCategory(@Valid @ModelAttribute Category category,BindingResult result) {
		
		if(result.hasErrors()) {
			return "category-form";
		}
		
		categoryDAO.saveCategory(category);
		
		return "redirect:/administration/category-list";
	}
	
	
	@RequestMapping("/category-delete")
	public String deleteCategory(@RequestParam int id) {
		
		
		categoryDAO.deleteCategory(id);
		
		return "redirect:/administration/category-list";
	}
	
	//TAG !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	@RequestMapping("/tag-list")
	public String getTagList(Model model) {
		
		List<Tag> tagList = tagDAO.getTagList();
		
		model.addAttribute("tagList", tagList);
		
		return "tag-list";
	}

	@RequestMapping("/tag-form")
	public String getTagForm(Model model) {
		
		Tag tag = new Tag();

		model.addAttribute("tag",tag);
		
		return "tag-form";
	}
	
	@RequestMapping("/tag-save")
	public String saveTag(@Valid @ModelAttribute Tag tag,BindingResult result) {
		
		if(result.hasErrors()) {
			return "tag-form";
		}
		
		tagDAO.saveTage(tag);
		
		return "redirect:/administration/tag-list";
	}
	
	@RequestMapping("/tag-delete")
	public String deleteTag(@RequestParam int id) {
		tagDAO.deleteTag(id);
		return "redirect:/administration/tag-list";
	}
	
	@RequestMapping("/tag-form-update")
	public String getTagUpdateForm(@RequestParam int id,Model model) {
		
		Tag tag = tagDAO.getTag(id);
		model.addAttribute("tag", tag);
		return "tag-form";
	}
	
	//EMPLOYEE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	@RequestMapping("/employee-list")
	public String getEmployeeList(Model model) {
		
		model.addAttribute("employeeList",employeeDAO.getEmployeeList());
		
		return"employee-list";
	}
	
	@RequestMapping("/employee-form")
	public String getEmployeeForm(Model model) {
	
		Employee employee = new Employee();
		
		model.addAttribute("employee",employee);
		
		return "employee-form";
	}
	
	@RequestMapping("/employee-form-update")
	public String getEmployeeUpdateForm(@RequestParam int id, Model model) {
		
		Employee employee = employeeDAO.getEmployee(id);
		
		model.addAttribute("employee", employee);
		
		return "employee-form";
	}
	
	@RequestMapping("/employee-save")
	public String saveCategory( @ModelAttribute Employee employee) {
		

		
		employeeDAO.saveEmployee(employee);
		
		return "redirect:/administration/employee-list";
	}
	
	@RequestMapping("/employee-delete")
	public String deleteEmployee(@RequestParam int id) {
		
		employeeDAO.deleteEmployee(id);
		
		return "redirect:/administration/employee-list";
	}
	
	//USER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!	
	
	@RequestMapping("/user-list")
	public String getUserListPage(Model model) {
		
		model.addAttribute("userList", userDAO.getUserList());
		model.addAttribute("user", userDAO.getUserByUsername("radovan"));
		
		return "user-list";
	}
	
	@RequestMapping("/user-enable")
	public String enableUser(@RequestParam String username) {
		
		userDAO.enableUser(username);
		
		return "redirect:/administration/user-list";
	}
	
	@RequestMapping("/user-form")
	public String getUserForm(Model model) {
		
		model.addAttribute("user", new User());
		model.addAttribute("roles", userDAO.getRoles());
		return "user-form";
		
	}
	
	@RequestMapping("/user-save")
	public String getUserForm(@ModelAttribute User user) {
		
		
		String passwordEncode = new BCryptPasswordEncoder().encode(user.getPassword());
		
		user.setEnabled(true);
		user.setPassword("{bcrypt}"+passwordEncode);
		
		userDAO.saveUser(user);
		
		return "redirect:/administration/user-list";
		
	}
	
	@RequestMapping("/user-delete")
	public String getUserForm(@RequestParam String username) {
		
		
		userDAO.deleteUser(username);
		
		return "redirect:/administration/user-list";
		
	}
	
	@RequestMapping("/user-edit-profile")
	public String getUserEditProfile(Principal principal, Model model) {
		
		
		User user = userDAO.getUserByUsername(principal.getName());
		model.addAttribute("user", user);
		
		return "user-edit-profile";
		
	}
	
	@RequestMapping("/user-edit")
	public String getUserEdit(@ModelAttribute User user) {
				
		userDAO.saveUser(user);
		
		return "redirect:/administration/user-list";
		
	}
	
	@RequestMapping("/user-change-password")
	public String getUserChangePassword(Principal principal, Model model) {	
		
		model.addAttribute("changePassword", new ChangePassword());
		
		return "user-change-password";
		
	}
	
	@RequestMapping("/user-change-password-action")
	public String getUserChangePasswordAction(@ModelAttribute ChangePassword changePassword, Principal principal, Model model) {	
		
		
		if(changePassword.getNewPassword().equalsIgnoreCase(changePassword.getConfirmPassword())) {
			User user = userDAO.getUserByUsername(principal.getName());
			
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			
			if(encoder.matches(changePassword.getOldPassword(), user.getPassword().replace("{bcrypt}", ""))) {
				
				user.setPassword("{bcrypt}"+encoder.encode(changePassword.getNewPassword()));
				
				userDAO.saveUser(user);
				
			}else {
				//nije unet dobar stari password ispisati
				return "user-change-password";
			}
		}else {
			
			//new poklapa se novi password i confirm password
			return "user-change-password";
		}
		
		User user = userDAO.getUserByUsername(principal.getName());
		model.addAttribute("changePassword", new ChangePassword());
		
		return "redirect:/administration/user-list";
		
	}
	
}
