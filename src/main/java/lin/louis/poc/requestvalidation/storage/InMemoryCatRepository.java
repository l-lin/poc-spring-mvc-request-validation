package lin.louis.poc.requestvalidation.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import lin.louis.poc.requestvalidation.Cat;


@Repository
public class InMemoryCatRepository implements CatRepository {
	private AtomicInteger id = new AtomicInteger(1);
	private List<Cat> cats = new ArrayList<>();

	public InMemoryCatRepository() {
		cats.add(new Cat(id.getAndIncrement(), "Tony", 1));
		cats.add(new Cat(id.getAndIncrement(), "Tadar Sauce", 2));
		cats.add(new Cat(id.getAndIncrement(), "Nyan cat", 7));
	}

	@Override
	public List<Cat> getAll() {
		return cats;
	}

	@Override
	public Optional<Cat> get(int id) {
		return cats.stream().filter(cat -> id == cat.getId()).findFirst();
	}

	@Override
	public Cat add(Cat cat) {
		Cat c = new Cat(id.getAndIncrement(), cat.getName(), cat.getAge());
		cats.add(c);
		return c;
	}
}
