package com.wewe.swiftaid.converter;

import com.wewe.swiftaid.entity.AmbulanceTeam;
import com.wewe.swiftaid.repository.AmbulanceTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAmbulanceTeamConverter implements Converter<String, AmbulanceTeam> {

    @Autowired
    private AmbulanceTeamRepository ambulanceTeamRepository;

    @Override
    public AmbulanceTeam convert(String source) {
        try {
            Long id = Long.parseLong(source);
            return ambulanceTeamRepository.findById(id).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

