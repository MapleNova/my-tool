queryStationsInfoResultNingboGov = {}

function queryStationsInfoResultNingboGov.query_stations_info(jsonStr)
    local jsonData, err = json.decode(jsonStr)
    if err then
        return nil, err
    end
    local convertedData = {
        PageNo = jsonData.pageNo,
        PageSize = jsonData.pageCount,
        ItemSize = jsonData.itemSize,
        StationInfos = {}
    }

    for _, station in ipairs(jsonData.stationInfos) do
        local stationInfo = {
            StationID = station.stationId,
            OperatorID = station.operatorId,
            EquipmentOwnerID = station.equipmentOwnerId,
            StationName = station.stationName,
            CountryCode = station.countryCode,
            AreaCode = station.areaCode,
            Address = station.address,
            StationTel = station.stationTel,
            ServiceTel = station.serviceTel,
            StationType = station.stationType,
            StationStatus = station.stationStatus,
            ParkNums = station.parkNums,
            StationLng = station.stationLng,
            StationLat = station.stationLat,
            SiteGuide = station.siteGuide,
            Construction = station.construction,
            MatchCars = station.matchCars,
            ParkInfo = station.parkInfo,
            BusineHours = station.busineHours,
            ElectricityFee = station.electricityFee,
            ServiceFee = station.serviceFee,
            ParkFee = station.parkFee,
            Payment = station.payment,
            SupportOrder = station.supportOrder,
            EquipmentInfos = {}
        }
        if station.pictures and #station.pictures ~= 0 then
            stationInfo.Pictures = station.pictures
        end
        for _, equipment in ipairs(station.equipmentInfos) do
            local equipmentInfo = {
                EquipmentID = equipment.equipmentTpid,
                ManufacturerID = equipment.manufacturerId,
                ManufacturerName = equipment.manufacturerName,
                EquipmentModel = equipment.equipmentModel,
                ProductionDate = equipment.productionDate,
                EquipmentType = equipment.equipmentType,
                EquipmentLng = equipment.equipmentLng,
                EquipmentLat = equipment.equipmentLat,
                Power = equipment.power,
                EquipmentName = equipment.equipmentName,
                ConnectorInfos = {}
            }

            for _, connector in ipairs(equipment.connectorInfos) do
                local connectorInfo = {
                    ConnectorID = connector.connectorTpid,
                    ConnectorName = connector.connectorName,
                    ConnectorType = connector.connectorType,
                    VoltageUpperLimits = connector.voltageUpperLimits,
                    VoltageLowerLimits = connector.voltageLowerLimits,
                    Current = connector.current,
                    Power = connector.power,
                    ParkNo = connector.parkNo,
                    NationalStandard = connector.nationalStandard,
                }
                table.insert(equipmentInfo.ConnectorInfos, connectorInfo)
            end
            table.insert(stationInfo.EquipmentInfos, equipmentInfo)
        end
        table.insert(convertedData.StationInfos, stationInfo)
    end

    return json.encode(convertedData)

end
