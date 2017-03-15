import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AppSettings} from "../../_common/app.settings";
import {HttpClient} from "../http-client";
import {ErrorService} from "../error.service";
import {UserService} from "../user.service";
import {Headers, Response, URLSearchParams} from "@angular/http";
import {Shapes} from "../../_model/shapes";

@Injectable()
export class AdminShapeService {

  static SHAPE_URL = 'admin/shape';

  constructor(private http: HttpClient, private errorService: ErrorService, private userService: UserService) { }

  getShapes(filter: Shapes, offset: number, limit: number, order: string): Observable<Response> {
    let headers = new Headers();
    headers.append(AppSettings.OFFSET_HEADER, offset + '');
    headers.append(AppSettings.LIMIT_HEADER, limit + '');
    headers.append(AppSettings.ORDER_HEADER, order);

    let params: URLSearchParams = new URLSearchParams();
    if(filter.id) params.set('id', filter.id);

    return this.http
        .get(this.userService.getApiPrefix() + AdminShapeService.SHAPE_URL, params, headers)
        .map((response => response))
        .catch(err => this.errorService.handleServerError(err));
  }

  getShape(id: string): Observable<Shapes> {
    const url = this.userService.getApiPrefix() + `${AdminShapeService.SHAPE_URL}/${id}`;
    return this.http.get(url)
        .map((response => response.json() as Shapes))
        .catch(err => this.errorService.handleServerError(err));
  }

  update(shape: Shapes): Observable<Shapes> {
    const url = this.userService.getApiPrefix() + `${AdminShapeService.SHAPE_URL}/${shape.id}`;
    return this.http.put(url, JSON.stringify(shape))
        .map((response => response.json() as Shapes))
        .catch(err => this.errorService.handleServerError(err));
  }

  create(shape: Shapes): Observable<Shapes> {
    const url = this.userService.getApiPrefix() + AdminShapeService.SHAPE_URL;
    return this.http.post(url, JSON.stringify(shape))
        .map((response => response.json() as Shapes))
        .catch(err => this.errorService.handleServerError(err));
  }

  delete(id: string): Observable<void> {
    const url = this.userService.getApiPrefix() + `${AdminShapeService.SHAPE_URL}/${id}`;
    return this.http.delete(url)
        .map(() => null)
        .catch(err => this.errorService.handleServerError(err));
  }

}