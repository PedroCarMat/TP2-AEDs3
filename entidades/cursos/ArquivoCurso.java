package entidades.cursos;
import aed3.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ArquivoCurso extends Arquivo<Curso> {
    
    HashExtensivel<ParCodigoId> indiceCodigo;
    ArvoreBMais<ParNomeId> indiceNome;
    ArvoreBMais<ParIdId> indiceUsuarioCurso;

    public ArquivoCurso() throws Exception {
        super("curso", Curso.class.getConstructor());
        indiceCodigo = new HashExtensivel<>(
            ParCodigoId.class.getConstructor(),
            4, 
           "./dados/curso/indiceCodigo.d.db",
           "./dados/curso/indiceCodigo.c.db");
        indiceNome = new ArvoreBMais<>(
            ParNomeId.class.getConstructor(),
            4,
           "./dados/curso/indiceNome.db");
        indiceUsuarioCurso = new ArvoreBMais<>(
            ParIdId.class.getConstructor(),
            4, "./dados/curso/indiceUsuarioCurso.db");
    }

    @Override
    public int create(Curso c) throws Exception {
        // garante código único
        if(c.getCodigoCompartilhavel() == null || c.getCodigoCompartilhavel().length()==0) {
            String codigo;
            do {
                codigo = Curso.gerarCodigoCompartilhavel();
            } while(indiceCodigo.read(Math.abs(codigo.hashCode()))!=null);
            c.setCodigoCompartilhavel(codigo);
        } else {
            while(indiceCodigo.read(Math.abs(c.getCodigoCompartilhavel().hashCode()))!=null) {
                c.setCodigoCompartilhavel(Curso.gerarCodigoCompartilhavel());
            }
        }

        int id = super.create(c);
        indiceCodigo.create(new ParCodigoId(c.getCodigoCompartilhavel(), id));
        indiceNome.create(new ParNomeId(c.getNome(), id));
        indiceUsuarioCurso.create(new ParIdId(c.getIdUsuario(), id));
        return id;
    }

    public Curso readCodigo(String codigo) throws Exception {
        ParCodigoId pci = indiceCodigo.read(Math.abs(codigo.hashCode()));
        if(pci == null)
            return null;
        Curso c = read(pci.getId());
        return c;
    }

    public Curso[] readNome(String nome) throws Exception {
        ArrayList<ParNomeId> pnis = indiceNome.read(new ParNomeId(nome,-1));  
        if(pnis.isEmpty())
            return new Curso[0];

        Curso[] cursos = new Curso[pnis.size()];
        int i=0;
        for (ParNomeId pni : pnis) {
            cursos[i++] = super.read(pni.getId());            
        }
        return cursos;
    }

    public Curso[] readUsuario(int idUsuario) throws Exception {
        // Retorna cursos do usuário ordenados alfabeticamente pelo nome
        ArrayList<ParNomeId> pnis = indiceNome.read(null);
        if(pnis.isEmpty())
            return new Curso[0];

        ArrayList<Curso> lista = new ArrayList<>();
        for(ParNomeId pni : pnis) {
            Curso c = super.read(pni.getId());
            if(c!=null && c.getIdUsuario()==idUsuario)
                lista.add(c);
        }
        Curso[] resultado = new Curso[lista.size()];
        lista.toArray(resultado);
        return resultado;
    }

    public Curso[] readAll() throws Exception {
        ArrayList<ParNomeId> pnis = indiceNome.read(null);
        if(pnis.isEmpty())
            return new Curso[0];

        Curso[] cursos = new Curso[pnis.size()];
        int i=0;
        for (ParNomeId pni : pnis) {
            cursos[i++] = super.read(pni.getId());            
        }
        return cursos;
    }

    public Curso[] readAllByDate() throws Exception {
        ArrayList<ParNomeId> pnis = indiceNome.read(null);
        if(pnis.isEmpty())
            return new Curso[0];

        ArrayList<Curso> lista = new ArrayList<>();
        for (ParNomeId pni : pnis) {
            Curso c = super.read(pni.getId());
            if(c!=null) lista.add(c);
        }
        Collections.sort(lista, new Comparator<Curso>() {
            public int compare(Curso a, Curso b) {
                return a.getDataInicio().compareTo(b.getDataInicio());
            }
        });
        Curso[] cursos = new Curso[lista.size()];
        lista.toArray(cursos);
        return cursos;
    }
    
    @Override
    public boolean delete(int id) throws Exception {
        Curso c = read(id);
        if(c!=null)
            if(super.delete(id)) {
                indiceCodigo.delete(Math.abs(c.getCodigoCompartilhavel().hashCode()));
                indiceNome.delete(new ParNomeId(c.getNome(), c.getID()));
                indiceUsuarioCurso.delete(new ParIdId(c.getIdUsuario(), c.getID()));
                return true;
            }
        return false;
    }

    @Override
    public boolean update(Curso novoCurso) throws Exception {
        Curso c = read(novoCurso.getID());
        if(c==null)
            return false;
        if(super.update(novoCurso)) {
            if(c.getCodigoCompartilhavel().compareTo(novoCurso.getCodigoCompartilhavel())!=0) {
                indiceCodigo.delete(Math.abs(c.getCodigoCompartilhavel().hashCode()));
                indiceCodigo.create(new ParCodigoId(novoCurso.getCodigoCompartilhavel(), novoCurso.getID()));
            }
            if(c.getNome().compareTo(novoCurso.getNome())!=0) {
                indiceNome.delete(new ParNomeId(c.getNome(), c.getID()));
                indiceNome.create(new ParNomeId( novoCurso.getNome(), novoCurso.getID()));
            }
            if(c.getIdUsuario() != novoCurso.getIdUsuario()) {
                indiceUsuarioCurso.delete(new ParIdId(c.getIdUsuario(), c.getID()));
                indiceUsuarioCurso.create(new ParIdId(novoCurso.getIdUsuario(), novoCurso.getID()));
            }
            return true;
        }
        return false;
    }


    public void close() throws Exception {
        super.close();
        indiceCodigo.close();
        indiceNome.close();
        indiceUsuarioCurso.close();
    }
}
