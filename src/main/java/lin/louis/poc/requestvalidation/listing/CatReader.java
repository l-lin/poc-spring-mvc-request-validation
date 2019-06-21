package lin.louis.poc.requestvalidation.listing;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lin.louis.poc.requestvalidation.storage.CatRepository;
import lin.louis.poc.requestvalidation.Cat;


@Service
public class CatReader {

	private final CatRepository repo;

	public CatReader(CatRepository repo) {this.repo = repo;}

	public List<Cat> getAll() {
		return repo.getAll();
	}

	public Optional<Cat> get(int id) {
		return repo.get(id);
	}

	public List<Cat> searchByName(String name) {
		return getAll().stream().filter(c -> name.equalsIgnoreCase(c.getName())).collect(Collectors.toList());
	}
}
