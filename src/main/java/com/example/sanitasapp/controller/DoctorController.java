package com.example.sanitasapp.controller;

import com.example.sanitasapp.models.Doctor;
import com.example.sanitasapp.models.Users;
import com.example.sanitasapp.services.ServicesImpl.AppointmentSerImpl;
import com.example.sanitasapp.services.ServicesImpl.DoctorSerImpl;
import com.example.sanitasapp.services.ServicesImpl.UsersSerImpel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DoctorController {

    private final DoctorSerImpl doctorServices;
    private final AppointmentSerImpl appointmentServices;
    private final UsersSerImpel usersServices;

    @Autowired
    public DoctorController(DoctorSerImpl doctorServices, AppointmentSerImpl appointmentServices, UsersSerImpel usersServices) {
        this.doctorServices = doctorServices;
        this.appointmentServices = appointmentServices;
        this.usersServices = usersServices;
    }



    @GetMapping("/addDoctor")
    public String addingDoctor(Model model){
        Doctor doctor = new Doctor();
        model.addAttribute("doctor", doctor);
        return "add-doctor";
    }

    @PostMapping("/addDoctor")
    public String addDoctor(@ModelAttribute("doctor") Doctor doctor){
        Users user = Users.builder().username(doctor.getUsername()).password(doctor.getPassword()).build();
        usersServices.addUser(user);
        String data = doctor.getUsername();
        doctor.setUsername(data + "@doctor.com");
        doctorServices.addDoctorToList(doctor);
        return "redirect:/viewDoctors";
    }

    @GetMapping("/viewDoctors")
    public String viewAppointments(Model model){
        model.addAttribute("allDoctors", doctorServices.getAllDoctor());
        return "doctors";
    }

    @GetMapping("/deleteDoctor/{doctorId}")
    public String deletePatient(@PathVariable Long doctorId){
        appointmentServices.deleteUsingDoctorId(doctorId);
        doctorServices.removeDoctor(doctorId);
        return "redirect:/viewDoctors";
    }
}
