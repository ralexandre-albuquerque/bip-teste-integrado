import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { TransferRequest } from '../models/transfer-request';
import { Beneficio } from '../models/beneficio';

@Injectable({
  providedIn: 'root'
})
export class BeneficioService {
  private readonly API_URL = 'http://localhost:8080/api/v1/beneficios';

  constructor(private http: HttpClient) { }

  transfer(request: TransferRequest): Observable<void> {
    // Forçamos o cabeçalho JSON para evitar que o navegador se perca
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.post<void>(`${this.API_URL}/transferencias`, request, { headers });
  }

  list(): Observable<Beneficio[]> {
     return this.http.get<any>(this.API_URL).pipe(
       map(response => {
         // LOG PARA DIAGNÓSTICO: Abra o F12 no navegador e veja se isso aparece rápido
         console.log('Resposta bruta do Java:', response);

         // Se o Java retorna PagedModel, a lista está em .content
         // Se retornar a lista direta, usa a response
         const lista = response.content || response;

         console.log('Lista processada para o componente:', lista);
         return lista;
       })
     );
   }

  create(beneficio: Partial<Beneficio>): Observable<Beneficio> {
    return this.http.post<Beneficio>(this.API_URL, beneficio);
  }

  delete(id: number): Observable<void> {
      return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  update(id: number, beneficio: Partial<Beneficio>): Observable<Beneficio> {
    return this.http.put<Beneficio>(`${this.API_URL}/${id}`, beneficio);
  }
}
