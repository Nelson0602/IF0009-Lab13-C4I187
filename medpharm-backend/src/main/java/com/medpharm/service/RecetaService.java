package com.medpharm.service;

import com.medpharm.dto.DetalleRequestDTO;
import com.medpharm.dto.DetalleResponseDTO;
import com.medpharm.dto.RecetaRequestDTO;
import com.medpharm.dto.RecetaResponseDTO;
import com.medpharm.exception.BusinessRuleException;
import com.medpharm.exception.ResourceNotFoundException;
import com.medpharm.model.DetalleReceta;
import com.medpharm.model.Medicamento;
import com.medpharm.model.RecetaMedica;
import com.medpharm.model.Usuario;
import com.medpharm.repository.MedicamentoRepository;
import com.medpharm.repository.RecetaMedicaRepository;
import com.medpharm.repository.UsuarioRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Lógica de negocio de las recetas: consulta, registro y cambio de estado
@Service
public class RecetaService {

    private static final String STATUS_PENDING = "PENDIENTE";
    private static final String STATUS_DISPATCHED = "DESPACHADA";
    private static final String STATUS_CANCELLED = "CANCELADA";

    private final RecetaMedicaRepository recetaRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final UsuarioRepository usuarioRepository;

    public RecetaService(RecetaMedicaRepository recetaRepository,
            MedicamentoRepository medicamentoRepository,
            UsuarioRepository usuarioRepository) {
        this.recetaRepository = recetaRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<RecetaResponseDTO> findAll() {
        return recetaRepository.findAllByOrderByIdDesc().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<RecetaResponseDTO> findByStatus(String status) {
        String normalized = status.trim().toUpperCase();
        if (!isKnownStatus(normalized)) {
            throw new IllegalArgumentException("Estado no válido: " + status);
        }
        return recetaRepository.findByEstadoOrderByIdDesc(normalized).stream().map(this::toResponse).toList();
    }

    // Registra una receta nueva a nombre del médico autenticado y revisa el stock
    @Transactional
    public RecetaResponseDTO create(RecetaRequestDTO request, String doctorUsername) {
        Usuario doctor = usuarioRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + doctorUsername));

        // Si el mismo medicamento aparece en dos renglones, se suman las cantidades
        Map<Long, Integer> requestedByMedicine = new HashMap<>();
        for (DetalleRequestDTO item : request.getDetalles()) {
            requestedByMedicine.merge(item.getMedicamentoId(), item.getCantidad(), Integer::sum);
        }

        RecetaMedica prescription = new RecetaMedica();
        prescription.setCodigoReceta(buildPrescriptionCode());
        prescription.setPacienteNombre(request.getPacienteNombre().trim());
        prescription.setMedico(doctor);
        prescription.setEstado(STATUS_PENDING);
        prescription.setFechaEmision(LocalDateTime.now());

        for (DetalleRequestDTO item : request.getDetalles()) {
            Medicamento medicine = medicamentoRepository.findById(item.getMedicamentoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Medicamento no encontrado: " + item.getMedicamentoId()));

            int totalRequested = requestedByMedicine.get(medicine.getId());
            if (medicine.getStock() < totalRequested) {
                throw new BusinessRuleException("Stock insuficiente de " + medicine.getNombre()
                        + ": disponible " + medicine.getStock() + ", solicitado " + totalRequested);
            }

            DetalleReceta line = new DetalleReceta();
            line.setMedicamento(medicine);
            line.setCantidad(item.getCantidad());
            line.setDosisIndicada(item.getDosisIndicada().trim());
            prescription.addDetalle(line);
        }

        return toResponse(recetaRepository.save(prescription));
    }

    // Cambia el estado a DESPACHADA o CANCELADA; al despachar se descuenta el stock
    @Transactional
    public RecetaResponseDTO updateStatus(Long id, String newStatus) {
        String normalized = newStatus.trim().toUpperCase();
        if (!normalized.equals(STATUS_DISPATCHED) && !normalized.equals(STATUS_CANCELLED)) {
            throw new IllegalArgumentException("El estado debe ser DESPACHADA o CANCELADA");
        }

        RecetaMedica prescription = recetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receta no encontrada: " + id));

        if (!STATUS_PENDING.equals(prescription.getEstado())) {
            throw new BusinessRuleException("Solo se puede modificar una receta en estado PENDIENTE");
        }

        if (normalized.equals(STATUS_DISPATCHED)) {
            for (DetalleReceta line : prescription.getDetalles()) {
                Medicamento medicine = line.getMedicamento();
                if (medicine.getStock() < line.getCantidad()) {
                    throw new BusinessRuleException("No hay stock suficiente de " + medicine.getNombre()
                            + " para despachar la receta");
                }
                medicine.setStock(medicine.getStock() - line.getCantidad());
            }
        }

        prescription.setEstado(normalized);
        return toResponse(prescription);
    }

    private boolean isKnownStatus(String status) {
        return status.equals(STATUS_PENDING) || status.equals(STATUS_DISPATCHED) || status.equals(STATUS_CANCELLED);
    }

    // Arma códigos como REC-2026-003 según la cantidad de recetas existentes
    private String buildPrescriptionCode() {
        long next = recetaRepository.count() + 1;
        return String.format("REC-%d-%03d", LocalDate.now().getYear(), next);
    }

    private RecetaResponseDTO toResponse(RecetaMedica prescription) {
        List<DetalleResponseDTO> lines = prescription.getDetalles().stream()
                .map(line -> new DetalleResponseDTO(
                        line.getMedicamento().getNombre(), line.getCantidad(), line.getDosisIndicada()))
                .toList();

        return new RecetaResponseDTO(
                prescription.getId(),
                prescription.getCodigoReceta(),
                prescription.getPacienteNombre(),
                prescription.getMedico().getNombreCompleto(),
                prescription.getEstado(),
                prescription.getFechaEmision(),
                lines);
    }
}