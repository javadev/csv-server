import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICsvfile, Csvfile } from 'app/shared/model/csvfile.model';
import { CsvfileService } from './csvfile.service';
import { CsvfileComponent } from './csvfile.component';
import { CsvfileDetailComponent } from './csvfile-detail.component';
import { CsvfileUpdateComponent } from './csvfile-update.component';

@Injectable({ providedIn: 'root' })
export class CsvfileResolve implements Resolve<ICsvfile> {
  constructor(private service: CsvfileService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICsvfile> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((csvfile: HttpResponse<Csvfile>) => {
          if (csvfile.body) {
            return of(csvfile.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Csvfile());
  }
}

export const csvfileRoute: Routes = [
  {
    path: '',
    component: CsvfileComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Csvfiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CsvfileDetailComponent,
    resolve: {
      csvfile: CsvfileResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Csvfiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CsvfileUpdateComponent,
    resolve: {
      csvfile: CsvfileResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Csvfiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CsvfileUpdateComponent,
    resolve: {
      csvfile: CsvfileResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Csvfiles'
    },
    canActivate: [UserRouteAccessService]
  }
];
