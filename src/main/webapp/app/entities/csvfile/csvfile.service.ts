import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICsvfile } from 'app/shared/model/csvfile.model';

type EntityResponseType = HttpResponse<ICsvfile>;
type EntityArrayResponseType = HttpResponse<ICsvfile[]>;

@Injectable({ providedIn: 'root' })
export class CsvfileService {
  public resourceUrl = SERVER_API_URL + 'api/csvfiles';

  constructor(protected http: HttpClient) {}

  create(csvfile: ICsvfile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(csvfile);
    return this.http
      .post<ICsvfile>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(csvfile: ICsvfile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(csvfile);
    return this.http
      .put<ICsvfile>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICsvfile>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICsvfile[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(csvfile: ICsvfile): ICsvfile {
    const copy: ICsvfile = Object.assign({}, csvfile, {
      date: csvfile.date && csvfile.date.isValid() ? csvfile.date.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? moment(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((csvfile: ICsvfile) => {
        csvfile.date = csvfile.date ? moment(csvfile.date) : undefined;
      });
    }
    return res;
  }
}
