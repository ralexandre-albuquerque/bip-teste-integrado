import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
// Importe as ferramentas de rota aqui
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  // ADICIONE ESTES TRÊS ABAIXO:
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class AppComponent {
  title = 'beneficios-app';
}
