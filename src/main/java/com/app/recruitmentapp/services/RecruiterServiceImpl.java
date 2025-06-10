package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Recruiter;
import com.app.recruitmentapp.entities.Role;
import com.app.recruitmentapp.repositories.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecruiterServiceImpl implements RecruiterService {

    @Autowired
    private RecruiterRepository recruiterRepository;
    @Override
    public List<Recruiter> getAllRecruiters() {
        return recruiterRepository.findAll();
    }

    @Override
    public Optional<Recruiter> getRecruiterById(Long id) {
        return recruiterRepository.findById(id);
    }

    @Override
    public Recruiter saveRecruiter(Recruiter recruiter) {
        recruiter.setActive(false);
        recruiter.setRole(Role.RECRUITER);
        return recruiterRepository.save(recruiter);
    }

    @Override
    public Recruiter updateRecruiter(Long id, Recruiter newRecruiter) {
         Recruiter r = recruiterRepository.findById(id).orElse(null);
         r.setName(newRecruiter.getName());
         r.setFirstName(newRecruiter.getFirstName());
         r.setActive(newRecruiter.getActive());
         r.setAddress(newRecruiter.getAddress());
         r.setEmail(newRecruiter.getEmail());
         r.setPassword(newRecruiter.getPassword());
         r.setRole(newRecruiter.getRole());
         r.setCreationDate(newRecruiter.getCreationDate());
         r.setOfferList(newRecruiter.getOfferList());
         recruiterRepository.saveAndFlush(r);
         return r;
    }
    @Override
    public void deleteRecruiter(Long id) {
        if (recruiterRepository.existsById(id)) {
            recruiterRepository.deleteById(id);
        } else {
            throw new RuntimeException("Recruteur non trouvé");
        }
    }

}
