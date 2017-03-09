import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {AuthenticationService} from "../_service/authentication.service";
import {User} from "../_model/user";

@Component({
  moduleId: module.id,
  templateUrl: 'login.component.html'
})

export class LoginComponent implements OnInit {
  model: User = new User();
  loading = false;
  error = '';

  constructor(
      private router: Router,
      private authenticationService: AuthenticationService) { }

  ngOnInit() {
    // reset login status
    this.authenticationService.logout();
  }

  login() {
    this.loading = true;
    this.authenticationService.login(this.model.username, this.model.password)
        .subscribe(result => {
            this.router.navigate(['/timeTable']);
        },
        err  => {
            this.loginFailed();
        });
  }

  private loginFailed() {
      this.error = 'Username or password is incorrect';
      this.loading = false;
  }
}
