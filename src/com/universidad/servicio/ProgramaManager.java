//package com.universidad.servicio;
//
//import com.universidad.modelo.Programa;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ProgramaManager {
//    private List<Programa> programas;
//
//    public ProgramaManager() {
//        this.programas = new ArrayList<>();
//        // Cargar algunos programas de ejemplo (o luego de la BD)
//        programas.add(new Programa(1.0, "Ingeniería de Sistemas", 10.0, null, null));
//        programas.add(new Programa(2.0, "Ingeniería Industrial", 8.0, null, null));
//    }
//
//    public Programa buscarProgramaPorId(Long id) {
//        return programas.stream()
//                .filter(p -> p.getId().equals(id))
//                .findFirst()
//                .orElse(null);
//    }
//
//    public List<Programa> listarProgramas() {
//        return new ArrayList<>(programas);
//    }
//}
