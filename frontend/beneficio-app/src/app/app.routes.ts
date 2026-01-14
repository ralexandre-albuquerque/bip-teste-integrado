// src/app/app.routes.ts
import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'beneficios',
    loadComponent: () =>
      import('./components/beneficio-crud/beneficio-crud.component').then(m => m.BeneficioCrudComponent)
  },
  {
    path: 'transferir',
    loadComponent: () =>
      import('./components/transfer/transfer.component').then(m => m.TransferComponent)
  },
  { path: '', redirectTo: '/beneficios', pathMatch: 'full' },
  { path: '**', redirectTo: '/beneficios' }
];
