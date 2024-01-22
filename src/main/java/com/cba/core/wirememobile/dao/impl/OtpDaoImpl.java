package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.OtpDao;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.model.OnetimePassword;
import com.cba.core.wirememobile.model.User;
import com.cba.core.wirememobile.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OtpDaoImpl implements OtpDao {

    private final OtpRepository repository;

    @Override
    public OnetimePassword findByUser(User user) {
        return repository.findByUser(user).orElseThrow(() -> new NotFoundException("No OTP found"));
    }

    @Override
    public void deleteById(int id) {
        repository.deleteById(id);

    }

    @Override
    public OnetimePassword create(OnetimePassword onetimePassword) {
        return repository.save(onetimePassword);
    }
}
