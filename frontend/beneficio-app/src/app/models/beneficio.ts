export interface Beneficio {
  id: number;
  nome: string;
  descricao: string;
  valor: number; // Se o Java envia 'valor', mantenha 'valor'
  ativo: boolean;
}