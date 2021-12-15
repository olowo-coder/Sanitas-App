package com.example.sanitasapp.controller;

import com.example.sanitasapp.models.Patient;
import com.example.sanitasapp.models.Users;
import com.example.sanitasapp.services.ServicesImpl.AppointmentSerImpl;
import com.example.sanitasapp.services.ServicesImpl.PatientSerImpel;
import com.example.sanitasapp.services.ServicesImpl.UsersSerImpel;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class PatientController {
    private final PatientSerImpel patientServices;
    private final AppointmentSerImpl appointmentServices;
    private final UsersSerImpel usersServices;

    @Autowired
    public PatientController(PatientSerImpel patientServices, AppointmentSerImpl appointmentServices, UsersSerImpel usersServices) {
        this.patientServices = patientServices;
        this.appointmentServices = appointmentServices;
        this.usersServices = usersServices;
    }

    @GetMapping("/viewPatients")
    public String viewAppointments(Model model){
        model.addAttribute("allPatients", patientServices.getAllPatients());
        return "patients";
    }

    @GetMapping("/patientDashboard")
    public String viewDashboard(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        Patient patient = (Patient) session.getAttribute("person");
        model.addAttribute("patientRecord", appointmentServices.getAllAppointmentByPatientId(patient.getPatientId()));
        return "patientview";
    }

    @GetMapping("/addPatient")
    public String addingPatient(Model model){
        Patient patient = new Patient();
        model.addAttribute("patient", patient);
        return "add-patient";
    }

    @PostMapping("/addPatient")
    public String addPatient(@ModelAttribute("patient") Patient patient){
        Users  user = Users.builder().username(patient.getUsername()).password(patient.getPassword()).build();
        String data = patient.getUsername();
        patient.setUsername(data + "@patient.com");
        usersServices.addUser(user);
        patientServices.addPatientToList(patient);
        return "redirect:/viewPatients";
    }

    @GetMapping("/deletePatient/{patientId}")
    public String deletePatient(@PathVariable Long patientId){
        Optional<Patient> patientOptional = patientServices.getPatientById(patientId);
        usersServices.deleteUserByUsername(patientOptional.get().getUsername());
        appointmentServices.deleteUsingPatientId(patientId);
        patientServices.removePatient(patientId);

        return "redirect:/viewPatients";
    }
}
