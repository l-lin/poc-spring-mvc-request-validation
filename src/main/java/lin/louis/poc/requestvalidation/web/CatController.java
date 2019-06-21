package lin.louis.poc.requestvalidation.web;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lin.louis.poc.requestvalidation.adding.CatAdder;
import lin.louis.poc.requestvalidation.listing.CatReader;
import lin.louis.poc.requestvalidation.web.validation.AcceptedValues;
import lin.louis.poc.requestvalidation.Cat;


@RestController
@RequestMapping(path = "/cats")
@Validated // Just add this annotation and the validation is be performed
public class CatController {

	private final CatReader reader;

	private final CatAdder adder;

	public CatController(CatReader reader, CatAdder adder) {
		this.reader = reader;
		this.adder = adder;
	}

	@GetMapping
	public List<Cat> getAll() {
		return reader.getAll();
	}

	@GetMapping(path = "/{id}")
	public Cat getCat(@PathVariable @Min(1) int id) {
		return reader.get(id).orElseThrow(() -> new NullPointerException("Could not find cat with id " + id));
	}

	@GetMapping(path = "/search")
	public List<Cat> searchByName(@RequestParam @NotBlank @AcceptedValues(fields = { "Tony",
			"Nyan cat" }) String name) {
		return reader.searchByName(name);
	}

	@PostMapping
	public Cat add(@RequestBody @Valid Cat cat) {
		return adder.add(cat);
	}
}
