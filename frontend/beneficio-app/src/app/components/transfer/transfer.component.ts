import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { BeneficioService } from '../../services/beneficio.service';
import { Beneficio } from '../../models/beneficio';
import { TransferRequest } from '../../models/transfer-request';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './transfer.component.html',
  styleUrls: ['./transfer.component.scss'],
  providers: [DecimalPipe]
})
export class TransferComponent implements OnInit {
  transferForm: FormGroup;
  beneficios: Beneficio[] = [];
  loadingBeneficios = false;
  submitting = false;
  message = '';
  isError = false;

  constructor(
    private fb: FormBuilder,
    private beneficioService: BeneficioService,
    private decimalPipe: DecimalPipe
  ) {
    this.transferForm = this.fb.group({
      fromId: [null, [Validators.required]],
      toId: [null, [Validators.required]],
      amount: [null, [Validators.required, Validators.min(0.01)]]
    }, {
      validators: [this.originDifferentFromDestinationValidator]
    });
  }

  ngOnInit(): void {
    this.loadBeneficios();

    // Sempre que a origem mudar, atualizamos validações relacionadas ao amount
    this.transferForm.get('fromId')!.valueChanges.subscribe(() => {
      this.updateMaxAmountValidator();
    });
  }

  private loadBeneficios() {
    this.loadingBeneficios = true;
    this.beneficioService.list().subscribe({
      next: (list) => {
        this.beneficios = list || [];
        this.loadingBeneficios = false;
        // opcional: setar valores padrão se houver pelo menos 2 itens
        if (this.beneficios.length >= 2) {
          // não forçar se usuário já escolheu
          if (!this.transferForm.get('fromId')!.value) {
            this.transferForm.get('fromId')!.setValue(this.beneficios[0].id);
          }
          if (!this.transferForm.get('toId')!.value) {
            this.transferForm.get('toId')!.setValue(this.beneficios[1].id);
          }
        }
      },
      error: (err) => {
        this.loadingBeneficios = false;
        this.isError = true;
        this.message = err?.error?.message || 'Erro ao carregar benefícios';
      }
    });
  }

  // Validador que garante origem != destino
  private originDifferentFromDestinationValidator(group: AbstractControl) {
    const origin = group.get('fromId')!.value;
    const dest = group.get('toId')!.value;
    if (origin && dest && origin === dest) {
      return { sameAccount: true };
    }
    return null;
  }

  // Atualiza o validador do amount baseado no saldo da conta de origem selecionada
  private updateMaxAmountValidator() {
    const fromId = this.transferForm.get('fromId')!.value;
    const amountControl = this.transferForm.get('amount')!;
    if (!fromId) {
      amountControl.setValidators([Validators.required, Validators.min(0.01)]);
      amountControl.updateValueAndValidity();
      return;
    }

    const origin = this.beneficios.find(b => b.id === +fromId);
    if (origin && origin.valor != null) {
      const max = origin.valor;
      // custom max validator
      const maxValidator = (c: AbstractControl) => {
        const v = c.value;
        if (v == null || v === '') return null;
        return Number(v) > max ? { exceedBalance: { max } } : null;
      };
      amountControl.setValidators([Validators.required, Validators.min(0.01), maxValidator]);
    } else {
      amountControl.setValidators([Validators.required, Validators.min(0.01)]);
    }
    amountControl.updateValueAndValidity();
  }

  get originBalance(): number | null {
    const fromId = this.transferForm.get('fromId')!.value;
    const origin = this.beneficios.find(b => b.id === +fromId);
    return origin?.valor ?? null;
  }

  formattedBalance(value: number | null): string {
    if (value == null) return '-';
    // Formata com 2 casas decimais, usa DecimalPipe
    return this.decimalPipe.transform(value, '1.2-2') ?? value!.toFixed(2);
  }

  onSubmit(): void {
  if (this.transferForm.invalid) {
    this.transferForm.markAllAsTouched();
    return;
  }

  this.submitting = true;
  this.message = '';
  this.isError = false;

  const request: TransferRequest = {
    fromId: +this.transferForm.value.fromId,
    toId: +this.transferForm.value.toId,
    amount: +this.transferForm.value.amount
  };

  this.beneficioService.transfer(request).subscribe({
    next: () => {
      this.submitting = false;
      this.message = 'Transferência realizada com sucesso!';
      this.isError = false;
      
      // 1. Limpa o campo de valor
      this.transferForm.get('amount')!.reset();

      // 2. ATUALIZAÇÃO LOCAL (O segredo para não travar)
      // Atualizamos os valores no array local para o usuário ver a mudança NA HORA
      const origin = this.beneficios.find(b => b.id === +request.fromId);
      const dest = this.beneficios.find(b => b.id === +request.toId);
      
      if (origin) origin.valor = Number(origin.valor) - Number(request.amount);
      if (dest) dest.valor = Number(dest.valor) + Number(request.amount);

      // 3. SINCRONIZAÇÃO POSTERGADA
      // Em vez de chamar o loadBeneficios() agora (que está travando), 
      // deixamos o navegador "respirar" por 2 segundos.
      setTimeout(() => {
        this.loadBeneficios();
      }, 2000);
    },
    error: (err) => {
      this.submitting = false;
      this.isError = true;
      this.message = err?.error?.message || 'Erro ao processar transferência';
      
      // Mesmo em erro, é bom recarregar para garantir que o saldo em tela é o real
      this.loadBeneficios();
    }
  });
  }

  // Helpers para template
  hasError(controlName: string, errorKey?: string) {
    const c = this.transferForm.get(controlName);
    if (!c) return false;
    if (errorKey) return c.hasError(errorKey) && c.touched;
    return c.invalid && c.touched;
  }
}

