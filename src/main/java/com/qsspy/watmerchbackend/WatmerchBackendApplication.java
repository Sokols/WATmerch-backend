package com.qsspy.watmerchbackend;

import com.qsspy.watmerchbackend.entity.*;
import com.qsspy.watmerchbackend.exception.register.RegisterException;
import com.qsspy.watmerchbackend.repository.CategoryRepository;
import com.qsspy.watmerchbackend.repository.RoleRepository;
import com.qsspy.watmerchbackend.service.ProductService;
import com.qsspy.watmerchbackend.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Logger;

@SpringBootApplication
public class WatmerchBackendApplication {

	private static final Logger LOGGER = Logger.getLogger(WatmerchBackendApplication.class.getName());

	public static void main(String[] args) throws IOException, RegisterException {

		ApplicationContext app = SpringApplication.run(WatmerchBackendApplication.class, args);

		initializeData(app);
	}

	private static void initializeData(ApplicationContext app) throws IOException, RegisterException {

		//init usersg

		UserService userService = app.getBean(UserService.class);
		ProductService productService = app.getBean(ProductService.class);
		RoleRepository roleRepository = app.getBean(RoleRepository.class);
		CategoryRepository categoryRepository = app.getBean(CategoryRepository.class);

		initRoles(roleRepository);
		initCategories(categoryRepository);

		initUser(userService);
		initEmployee(userService);
		initAdmin(userService);

		initProducts(productService);

		LOGGER.info("Initial MySQL Data created succesfully!");
	}

	private static void initCategories(CategoryRepository categoryRepository) {

		categoryRepository.saveAll(
				Arrays.asList(
						new Category("Kubki"),
						new Category("Bluzy"),
						new Category("Koszulki")
				)
		);
	}

	private static void initRoles(RoleRepository roleRepository) {

		roleRepository.saveAll(
				Arrays.asList(
						new Role(Role.RoleType.USER),
						new Role(Role.RoleType.EMPLOYEE),
						new Role(Role.RoleType.ADMIN)
				)
		);
	}

	private static void initProducts(ProductService productService) throws IOException {

		int barcode = 1;

		List<Float> prices = new ArrayList<Float>(Arrays.asList(20.99f,10f,39.99f,99.99f,123.56f,23.0f));

		for(int i=0;i<10;i++) {

			Category category = new Category();
			category.setId(1); //Kubki

			ProductDetails details = new ProductDetails();
			details.setLongDescription("This is basic long description");
			details.setQuantityInStock(20 - i);

			ProductBasicDetails basicDetails = new ProductBasicDetails();
			basicDetails.setShortDescription("This is short description");
			basicDetails.setDiscountPercent(.1f);
			String filename = "cup" + (new Random().nextInt(3) + 1) + ".jpg";
			File image = new File("src\\main\\resources\\static\\images\\product_basic_images\\cups\\" + filename);
			basicDetails.setLogoImage(Files.readAllBytes(image.toPath()));

			Product product = new Product();
			product.setBarcode(Integer.toString(barcode));
			product.setName("Kubek" + i);
			product.setPrice(prices.get(i % prices.size()));
			product.setVat(0.23f);
			product.setCategory(category);
			product.setBasicDetails(basicDetails);
			product.setDetails(details);
			productService.postProduct(product);
			barcode++;
		}

		for(int i=0;i<10;i++) {

			Category category = new Category();
			category.setId(2); //bluzy

			ProductDetails details = new ProductDetails();
			details.setLongDescription("This is basic long description");
			details.setQuantityInStock(20 - i);

			ProductBasicDetails basicDetails = new ProductBasicDetails();
			basicDetails.setShortDescription("This is short description");
			basicDetails.setDiscountPercent(.1f);
			String filename = "bluse" + (new Random().nextInt(3) + 1) + ".jpg";
			File image = new File("src\\main\\resources\\static\\images\\product_basic_images\\sweatshirts\\" + filename);
			basicDetails.setLogoImage(Files.readAllBytes(image.toPath()));

			Product product = new Product();
			product.setBarcode(Integer.toString(barcode));
			product.setName("Bluza" + i);
			product.setPrice(prices.get(i % prices.size()));
			product.setVat(0.23f);
			product.setCategory(category);
			product.setBasicDetails(basicDetails);
			product.setDetails(details);
			productService.postProduct(product);
			barcode++;
		}

		for(int i=0;i<10;i++) {

			Category category = new Category();
			category.setId(3); //koszulki

			ProductDetails details = new ProductDetails();
			details.setLongDescription("This is basic long description");
			details.setQuantityInStock(20 - i);

			ProductBasicDetails basicDetails = new ProductBasicDetails();
			basicDetails.setShortDescription("This is short description");
			basicDetails.setDiscountPercent(.1f);
			String filename = "tshirt" + (new Random().nextInt(3) + 1) + ".jpg";
			File image = new File("src\\main\\resources\\static\\images\\product_basic_images\\tshirts\\" + filename);
			basicDetails.setLogoImage(Files.readAllBytes(image.toPath()));

			Product product = new Product();
			product.setBarcode(Integer.toString(barcode));
			product.setName("Koszulka" + i);
			product.setPrice(prices.get(i % prices.size()));
			product.setVat(0.23f);
			product.setCategory(category);
			product.setBasicDetails(basicDetails);
			product.setDetails(details);
			productService.postProduct(product);
			barcode++;
		}
	}

	private static void initAdmin(UserService userService) throws IOException, RegisterException {

		Address shippingAddress = new Address();
		shippingAddress.setFirstName("Krystyna");
		shippingAddress.setLastName("Janda");
		shippingAddress.setStreet("ul. Testowa 17");
		shippingAddress.setPostalCode("12-345");
		shippingAddress.setCity("Białystok");
		shippingAddress.setCountry("Poland");
		shippingAddress.setPhoneNumber("123123123");
		shippingAddress.setState("Podlasie");

		Address billingAddress = new Address();
		billingAddress.setFirstName("Krystyna");
		billingAddress.setLastName("Janda");
		billingAddress.setStreet("ul. Testowa 17");
		billingAddress.setPostalCode("12-345");
		billingAddress.setCity("Warsaw");
		billingAddress.setCountry("Poland");
		billingAddress.setPhoneNumber("475198678");
		billingAddress.setState("Lubelskie");

		ShopUserDetails details = new ShopUserDetails();
		details.setFirstName("Krystyna");
		details.setLastName("Janda");
		details.setPhoneNumber("123123123");
		details.setBirthDate(new Date());
		details.setCompany("Szczepionkowcy");
		details.setNip("000999888");
		File image = new File("src\\main\\resources\\static\\images\\users_basic_images\\admin.jpg");
		details.setAvatar(Files.readAllBytes(image.toPath()));

		Role role = new Role();
		role.setId(3);


		ShopUser user = new ShopUser();
		user.setUsername("admin");
		user.setPassword("admin");
		user.setEmail("admin@mail.com");
		user.setEnabled(true);
		user.setShippingAddress(shippingAddress);
		user.setBillingAddress(billingAddress);
		user.setUserDetails(details);
		user.setRole(role);

		userService.register(user);
	}

	private static void initEmployee(UserService userService) throws IOException, RegisterException {

		Address shippingAddress = new Address();
		shippingAddress.setFirstName("Krzysztof");
		shippingAddress.setLastName("Kononowisz");
		shippingAddress.setStreet("ul. Szkolna 17");
		shippingAddress.setPostalCode("12-345");
		shippingAddress.setCity("Białystok");
		shippingAddress.setCountry("Poland");
		shippingAddress.setPhoneNumber("123123123");
		shippingAddress.setState("Podlasie");

		Address billingAddress = new Address();
		billingAddress.setFirstName("Krzysztof");
		billingAddress.setLastName("Kononowisz");
		billingAddress.setStreet("ul. Szkolna 17");
		billingAddress.setPostalCode("12-345");
		billingAddress.setCity("Warsaw");
		billingAddress.setCountry("Poland");
		billingAddress.setPhoneNumber("475198678");
		billingAddress.setState("Lubelskie");

		ShopUserDetails details = new ShopUserDetails();
		details.setFirstName("Krzysztof");
		details.setLastName("Kononowisz");
		details.setPhoneNumber("123123123");
		details.setBirthDate(new Date());
		details.setCompany("Koksy");
		details.setNip("999999999");
		File image = new File("src\\main\\resources\\static\\images\\users_basic_images\\employee.jpg");
		details.setAvatar(Files.readAllBytes(image.toPath()));

		Role role = new Role();
		role.setId(2);


		ShopUser user = new ShopUser();
		user.setUsername("employee");
		user.setPassword("employee");
		user.setEmail("employee@mail.com");
		user.setEnabled(true);
		user.setShippingAddress(shippingAddress);
		user.setBillingAddress(billingAddress);
		user.setUserDetails(details);
		user.setRole(role);

		userService.register(user);
	}

	private static void initUser(UserService userService) throws IOException, RegisterException {

		Address shippingAddress = new Address();
		shippingAddress.setFirstName("Adam");
		shippingAddress.setLastName("Abacki");
		shippingAddress.setStreet("ul. koksa 29");
		shippingAddress.setPostalCode("08-550");
		shippingAddress.setCity("Warsaw");
		shippingAddress.setCountry("Poland");
		shippingAddress.setPhoneNumber("475198678");
		shippingAddress.setState("Lubelskie");

		Address billingAddress = new Address();
		billingAddress.setFirstName("Adam");
		billingAddress.setLastName("Abacki");
		billingAddress.setStreet("ul. koksa 29");
		billingAddress.setPostalCode("08-550");
		billingAddress.setCity("Warsaw");
		billingAddress.setCountry("Poland");
		billingAddress.setPhoneNumber("475198678");
		billingAddress.setState("Lubelskie");

		ShopUserDetails details = new ShopUserDetails();
		details.setFirstName("Adam");
		details.setLastName("Abacki");
		details.setPhoneNumber("475198678");
		details.setBirthDate(new Date());
		details.setCompany("Ziomy");
		details.setNip("123123123");
		File image = new File("src\\main\\resources\\static\\images\\users_basic_images\\user.jpg");
		details.setAvatar(Files.readAllBytes(image.toPath()));

		Role role = new Role();
		role.setId(1);


		ShopUser user = new ShopUser();
		user.setUsername("user");
		user.setPassword("user");
		user.setEmail("user@mail.com");
		user.setEnabled(true);
		user.setShippingAddress(shippingAddress);
		user.setBillingAddress(billingAddress);
		user.setUserDetails(details);
		user.setRole(role);

		userService.register(user);
	}
}
