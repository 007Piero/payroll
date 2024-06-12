package payroll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeRepository eRepository;

    @Autowired
    private EmployeeModelAssembler assembler;

    /*
        // Mon endpoint de findAll() before REST
    @GetMapping("/employees")
    List<Employee> all() {
        return  eRepository.findAll();
    }
     */
    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all() {

        List<EntityModel<Employee>> employees = eRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee){
        return eRepository.save(newEmployee);
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable Long id){

        /*
            // Ceci était notre return qui n'est pas vraiment du REST
        return eRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
         */
        Employee employee = eRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toModel(employee);
    }

    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id){
        return eRepository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return eRepository.save(employee);
                })
                .orElseGet(() -> {
                    return eRepository.save(newEmployee);
                });
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id){
        eRepository.deleteById(id);
    }
}
