package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.model.Merchant;

import java.sql.SQLException;

public interface MerchantDao {

    Merchant findByMerchantId(String merchantId) throws RuntimeException;
}
