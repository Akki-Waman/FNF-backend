package com.sipl.ticket.core.util;

import com.sipl.ticket.core.dao.entity.Setting;
import com.sipl.ticket.core.dao.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SettingUtil {

    private final SettingRepository settingRepository;

    @Transactional
    public String generateCode(String screenType) {
        Setting setting = settingRepository.findByScreenIgnoreCase(screenType)
                .orElseThrow(() -> new RuntimeException("Setting not found for screen type: " + screenType));

        String prefix = setting.getPrefix() != null ? setting.getPrefix() : screenType.substring(0, 2).toUpperCase() + "-";
        long nextCode = setting.getMaxCode() != null ? setting.getMaxCode() + 1 : 1L;

        int prefixLength = prefix.length();
        int sequenceLength = 13 - prefixLength;

        if (sequenceLength <= 0) {
            throw new RuntimeException("Prefix too long to generate 13-digit code");
        }

        String formattedSequence = String.format("%0" + sequenceLength + "d", nextCode);
        String fullCode = prefix + formattedSequence;

        // Update sequence
        setting.setMaxCode(nextCode);
        settingRepository.save(setting);

        return fullCode;
    }
}
