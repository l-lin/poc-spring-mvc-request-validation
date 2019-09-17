package lin.louis.poc.requestvalidation;

import javax.validation.constraints.NotBlank;

import lin.louis.poc.requestvalidation.web.validation.AcceptedValues;


public class Cat {
	private int id;
	@NotBlank
	@AcceptedValues(fields = {"Tony", "Nyan cat", "Miaou"})
	private String name;
	private int age;

	public Cat() {
	}

	public Cat(int id, String name, int age) {
		this.id = id;
		this.name = name;
		this.age = age;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
