package net.njit.ms.cs.service;

import lombok.extern.slf4j.Slf4j;
import net.njit.ms.cs.exception.BadRequestRequestException;
import net.njit.ms.cs.exception.ResourceNotCreatedException;
import net.njit.ms.cs.exception.ResourceNotDeletedException;
import net.njit.ms.cs.exception.ResourceNotFoundException;
import net.njit.ms.cs.model.dto.request.TeachingAssistantDto;
import net.njit.ms.cs.model.entity.*;
import net.njit.ms.cs.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TeachingAssistantService {

    private final TeachingAssistantRepository teachingAssistantRepository;
    private final StaffRepository staffRepository;
    private final StudentRepository studentRepository;


    public TeachingAssistantService(TeachingAssistantRepository teachingAssistantRepository,
                                    StaffRepository staffRepository,
                                    StudentRepository studentRepository) {
        this.teachingAssistantRepository = teachingAssistantRepository;
        this.staffRepository = staffRepository;
        this.studentRepository = studentRepository;
    }

    public List<TeachingAssistant> getAllTeachingAssistant() {
        return this.teachingAssistantRepository.findAll();
    }

    public TeachingAssistant getTeachingAssistantById(TeachingAssistantId teachingAssistantId) {
        String ssn = teachingAssistantId.getSsn();
        String sid = teachingAssistantId.getSid();
        return this.teachingAssistantRepository.findById(teachingAssistantId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(
                        "TeachingAssistant with sid: %s and ssn: %s not found", sid, ssn)));
    }

    public TeachingAssistant getCreatedTeachingAssistant(TeachingAssistantDto teachingAssistantDto) {
        String ssn = teachingAssistantDto.getSsn();
        String sid = teachingAssistantDto.getSid();
        TeachingAssistantId teachingAssistantId = new TeachingAssistantId(ssn, sid);

        if (this.teachingAssistantRepository.existsById(teachingAssistantId)) {
            String message = String.format("TeachingAssistant with sid: %s and ssn: %s already exists.", sid, ssn);
            log.error(message);
            throw new BadRequestRequestException(message);
        }

        validateTeachingAssistant(teachingAssistantDto);
        return this.getCreateOrReplacedTeachingAssistant(this.getNewTeachingAssistant(teachingAssistantDto));
    }

    public TeachingAssistant getUpdatedTeachingAssistant(TeachingAssistantDto teachingAssistantDto) {
        String sid = teachingAssistantDto.getSid();
        String ssn = teachingAssistantDto.getSsn();

        TeachingAssistantId teachingAssistantId = new TeachingAssistantId(ssn, sid);
        TeachingAssistant teachingAssistant = this.getTeachingAssistantById(teachingAssistantId);

        return this.getCreateOrReplacedTeachingAssistant(this.getNewTeachingAssistant(teachingAssistantDto));
    }

    public void deleteTeachingAssistant(TeachingAssistantId teachingAssistantId) {
        TeachingAssistant teachingAssistant = this.getTeachingAssistantById(teachingAssistantId);
        try {
            log.info("{} {}", teachingAssistant.getSsn(), teachingAssistant.getSid());
            this.teachingAssistantRepository.delete(teachingAssistant);
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong deleting teachingAssistant with ssn: %s and sid: %s to backend",
                    teachingAssistantId.getSsn(), teachingAssistantId.getSid());
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotDeletedException(message);
        }
    }

    private TeachingAssistant getCreateOrReplacedTeachingAssistant(TeachingAssistant teachingAssistant) {
        try {
            return this.teachingAssistantRepository.save(teachingAssistant);
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong creating or replacing teachingAssistant with ssn: %s to backend",
                    teachingAssistant.getSsn());
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotCreatedException(message);
        }
    }

    private void validateTeachingAssistant(TeachingAssistantDto teachingAssistantDto) {
        String sid = teachingAssistantDto.getSid();
        String ssn = teachingAssistantDto.getSsn();

        Student foundStudent = this.studentRepository.findById(sid).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Student with sid: %s not found", sid)));
        String studentSsn = foundStudent.getSsn();

        if(!studentSsn.equals(teachingAssistantDto.getSsn())) {
            String message = String.format(
                    "Ssn in teachingAssistant: %s does not match student ssn: %s", ssn, studentSsn);
            log.error(message);
            throw new BadRequestRequestException(message);
        }

        Staff foundStaff = this.staffRepository.findById(ssn).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Staff with ssn: %s not found", sid)));
        String staffName = foundStaff.getName();

        if(!staffName.equals(foundStudent.getName())) {
            String message = String.format(
                    "Staff name %ss does not match student name: %s", staffName, foundStudent.getName());
            log.error(message);
            throw new BadRequestRequestException(message);
        }

    }

    private TeachingAssistant getNewTeachingAssistant(TeachingAssistantDto teachingAssistantDto) {
        TeachingAssistant teachingAssistant = new TeachingAssistant();
        teachingAssistant.setSid(teachingAssistantDto.getSid());
        teachingAssistant.setSsn(teachingAssistantDto.getSsn());
        teachingAssistant.setWorkHours(teachingAssistantDto.getWorkHours());
        return teachingAssistant;
    }

}