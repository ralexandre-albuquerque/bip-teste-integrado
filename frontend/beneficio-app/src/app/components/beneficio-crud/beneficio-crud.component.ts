import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BeneficioService } from '../../services/beneficio.service';
import { Beneficio } from '../../models/beneficio';
import { finalize } from 'rxjs/operators';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-beneficio-crud',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './beneficio-crud.component.html',
  styleUrls: ['./beneficio-crud.component.scss'],
  providers: [DecimalPipe]
})
export class BeneficioCrudComponent implements OnInit {
  form: FormGroup;
  beneficios: Beneficio[] = [];
  loading = false;
  submitting = false;
  message = '';
  isError = false;
  deletingId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private beneficioService: BeneficioService,
    private decimalPipe: DecimalPipe,
    private cdr: ChangeDetectorRef // Injeção para forçar a atualização da tela
  ) {
    this.form = this.fb.group({
      nome: ['', [Validators.required]],
      descricao: [''],
      valor: [0, [Validators.required, Validators.min(0)]],
      ativo: [true]
    });
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    console.log('Iniciando carregamento da lista...');
    this.loading = true;

    this.beneficioService.list()
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdr.detectChanges(); // Força o Angular a esconder o spinner
          console.log('Carregamento finalizado (loading = false)');
        })
      )
      .subscribe({
        next: (list) => {
          console.log('Dados recebidos no componente:', list);
          this.beneficios = [...list]; // Cria nova referência para o Angular detectar
          this.cdr.detectChanges(); // Força a renderização da tabela
        },
        error: (err) => {
          console.error('Erro ao carregar benefícios:', err);
          this.isError = true;
          this.message = err?.error?.message || 'Erro ao carregar benefícios do servidor';
        }
      });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting = true;
    this.message = '';
    this.isError = false;

    const payload: Partial<Beneficio> = {
      nome: this.form.value.nome,
      descricao: this.form.value.descricao,
      valor: Number(this.form.value.valor),
      ativo: this.form.value.ativo
    };

    this.beneficioService.create(payload)
      .pipe(finalize(() => {
        this.submitting = false;
        this.cdr.detectChanges();
      }))
      .subscribe({
        next: (created) => {
          this.message = 'Benefício criado com sucesso!';
          this.isError = false;
          this.form.reset({ nome: '', descricao: '', valor: 0, ativo: true });
          this.load(); // Recarrega a lista para garantir sincronia
        },
        error: (err) => {
          this.isError = true;
          this.message = err?.error?.message || 'Erro ao criar benefício';
        }
      });
  }

  confirmDelete(id: number): void {
    if (!confirm('Deseja realmente excluir este benefício?')) return;

    this.deletingId = id;
    this.beneficioService.delete(id)
      .pipe(finalize(() => {
        this.deletingId = null;
        this.cdr.detectChanges();
      }))
      .subscribe({
        next: () => {
          this.message = 'Benefício excluído com sucesso.';
          this.isError = false;
          this.load(); // Recarrega a lista
        },
        error: (err) => {
          this.isError = true;
          this.message = err?.error?.message || 'Erro ao excluir benefício';
        }
      });
  }

  formatted(value: number | null | undefined): string {
    if (value == null) return '0,00';
    return this.decimalPipe.transform(value, '1.2-2') ?? '0,00';
  }
}
