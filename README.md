# TP02 - Relacionamento N:N

**Alunos participantes:** Isabel Cristina, Laura Bargas, Pedro Mattar e Yuri Penido.

O **Busca de Cursos (TP02)** é uma evolução do sistema acadêmico para gestão de cursos livres da PUC Minas.  
Esta versão implementa o **Relacionamento N:N**, permitindo que usuários se inscrevam em múltiplos cursos.

O sistema utiliza persistência em arquivos binários de acesso aleatório (`RandomAccessFile`) e estruturas de dados avançadas, como **Tabelas Hash Extensível** para buscas diretas e **Árvores B+** para indexação e ordenação.

## Link do Vídeo
> Adicionar link aqui.

---

# Estrutura de Classes Criadas

## 1. Novas Classes Criadas (TP02)

### `Inscricao`
Entidade que representa o relacionamento N:N, armazenando os atributos:
- `idInscricao`
- `idUsuario` (aluno)
- `idCurso`
- `dataInscricao`

<img width="779" height="594" alt="Screenshot 2026-05-13 092207" src="https://github.com/user-attachments/assets/4dc5e9ef-f21f-4d6d-a845-df35f63bb401" />



### `ArquivoInscricao`
Classe responsável pelo CRUD das inscrições, gerenciando duas novas Árvores B+ para indexar o relacionamento:
- Índice por `idUsuario`
- Índice por `idCurso`

<img width="780" height="593" alt="Screenshot 2026-05-13 092226" src="https://github.com/user-attachments/assets/72e007f9-3277-44c3-bda0-8c5a7d50b332" />



### `ControleInscricao`
Gerencia a lógica de negócio das matrículas, permitindo que o usuário:
- Busque cursos
- Realize inscrições
- Cancele participações

<img width="1143" height="930" alt="Screenshot 2026-05-13 092428" src="https://github.com/user-attachments/assets/63f72a86-4f76-4352-ae76-7e174461742f" />



### `VisaoInscricao`
Interface de terminal para o menu de inscrições, responsável por:
- Exibir a lista de cursos em que o usuário está matriculado
- Mostrar a tela de detalhes para novas adesões

<img width="781" height="456" alt="Screenshot 2026-05-13 092456" src="https://github.com/user-attachments/assets/3c71e680-47ac-4128-a43c-e02b0bc07f16" />



### `ParIdId`
Classe de suporte que define o par de IDs utilizado nas Árvores B+ para vincular usuários e cursos de forma eficiente.

<img width="1029" height="932" alt="Screenshot 2026-05-13 092612" src="https://github.com/user-attachments/assets/8359ccfe-26ac-49c4-a2c4-d8261764d0ed" />



### `ParCodigoId`
Classe de suporte para o índice de busca rápida, vinculando o código alfanumérico (**NanoID**) ao ID do curso.

<img width="1135" height="932" alt="Screenshot 2026-05-13 092727" src="https://github.com/user-attachments/assets/d1ee1877-b56e-47ae-b9e3-4038f38faab8" />

---

# Operações Especiais Implementadas

## Exclusão Lógica e Reuso
O sistema não remove fisicamente o registro imediatamente.  
Em vez disso:
- Marca o registro com uma **lápide**
- Insere o endereço na lista de espaços disponíveis para reaproveitamento

<img width="778" height="662" alt="Screenshot 2026-05-13 092748" src="https://github.com/user-attachments/assets/8f8b919b-3cf2-49a3-b1c7-42891c095790" />



## Gestão de Inscritos
O gestor do curso pode:
- Visualizar a lista de inscritos
- Exportar os dados para o formato **CSV**

Isso facilita a gestão externa de alunos.

<img width="500" height="204" alt="Screenshot 2026-05-13 093112" src="https://github.com/user-attachments/assets/b3bfecac-fde7-41ec-b53f-c156b51226c6" />

<img width="840" height="534" alt="Screenshot 2026-05-13 093054" src="https://github.com/user-attachments/assets/ad812dea-0c38-471c-9674-6bb35624287c" />



## Sincronização de Índices
Ao criar uma inscrição, o sistema atualiza simultaneamente:
- Os índices diretos
- As árvores de relacionamento

Garantindo que a visualização de **"Minhas Inscrições"** seja imediata.

<img width="781" height="519" alt="Screenshot 2026-05-13 093128" src="https://github.com/user-attachments/assets/296c43b4-0f83-4807-b5b4-f5f855b8e77c" />

---

# Checklist

- Há um CRUD da entidade de associação `CursoUsuario` (que estende a classe `ArquivoIndexado`, adicionando Tabelas Hash Extensível e Árvores B+ como índices diretos e indiretos conforme necessidade) que funciona corretamente.
> Sim

- A visão de inscrições está corretamente implementada e permite consultas aos cursos em que um usuário está inscrito.
> Sim

- A visão de cursos funciona corretamente e permite a gestão dos usuários inscritos em um curso.
> Sim

- Há uma visualização dos cursos de outras pessoas por meio de um código NanoID.
> Sim

- A integridade do relacionamento entre cursos e usuários está mantida em todas as operações.
> Sim

- Operações de exclusão de curso ou usuário tratam corretamente as dependências na tabela de associação.
> Sim

- O trabalho compila corretamente.
> Sim

- O trabalho está completo e funcionando sem erros de execução.
> Sim

- O trabalho é original e não a cópia de um trabalho de outro grupo.
> Sim
