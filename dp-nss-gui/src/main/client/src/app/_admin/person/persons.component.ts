import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Person} from "../../_model/person";
import {AdminPersonService} from "../../_service/_admin/admin-person.service";

@Component({
  moduleId: module.id,
  selector: 'persons-component',
  templateUrl: './persons.component.html'
})
export class PersonsComponent implements OnInit {

  persons: Person[];

  constructor(private router: Router, private adminPersonService: AdminPersonService) {

  }

  ngOnInit(): void {
      this.adminPersonService.getPersons()
          .subscribe(response => {
              this.persons = response;

              //oznacim si, kdo je admin
              for(let person of this.persons) {
                  for(let role of person.roles) {
                      if(role === 'ADMIN') {
                          person.isAdmin = true;
                          break;
                      }
                  }
              }

          }, err  => {});
  }

  goToDetail(person: Person): void {
    this.router.navigate(['/person', person.id]);
  }

}
