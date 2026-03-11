package basics;

public class person {
	private String name;//mutable
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private int age;//immutable
	private address address;//mutable
	private phone phone;//mutable
	pet pet;
	public pet getPet() {
		return pet;
	}
	public void setPet(pet pet) {
		this.pet = pet;
	}
	

}
