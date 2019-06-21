package lin.louis.poc.requestvalidation.adding;

import org.springframework.stereotype.Service;

import lin.louis.poc.requestvalidation.storage.CatRepository;
import lin.louis.poc.requestvalidation.Cat;


@Service
public class CatAdder {
	private final CatRepository repo;

	public CatAdder(CatRepository repo) {this.repo = repo;}

	public Cat add(Cat cat) {
		return repo.add(cat);
	}
}
