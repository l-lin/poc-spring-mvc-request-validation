package lin.louis.poc.requestvalidation.storage;

import java.util.List;
import java.util.Optional;

import lin.louis.poc.requestvalidation.Cat;


public interface CatRepository {
	List<Cat> getAll();

	Optional<Cat> get(int id);

	Cat add(Cat cat);
}
