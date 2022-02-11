import Styled from 'styled-components';

const WizardWrapper = Styled.div`
    // padding: 25px 0;
    color:#eee;
    &.bordered-wizard{
        padding: 0;
    }
    .steps-action button.btn-next svg {
        ${({ theme }) => (theme.rtl ? 'margin-right' : 'margin-left')}: 10px;
        transform: rotateY(${({ theme }) => (theme.rtl ? '180deg' : '0deg')})
    }
    .steps-action button.btn-prev svg {
        ${({ theme }) => (!theme.rtl ? 'margin-right' : 'margin-left')}: 10px;
        transform: rotateY(${({ theme }) => (theme.rtl ? '180deg' : '0deg')})
    }
    .ant-steps {
        @media only screen and (max-width: 767px) {
            flex-flow: column;
            align-items: center;
        }
        @media only screen and (max-width: 480px) {
            align-items: flex-start;
        }
    }
    .ant-steps-item{
        overflow: revert !important;
    }
    .ant-steps-item-container{
        display: flex;
        flex-flow: column;
        align-items: center;
        width: 50%;
        ${({ theme }) => (!theme.rtl ? 'padding-left' : 'padding-right')}: 15px;
        @media only screen and (max-width: 991px) {
            width: 100%;
            ${({ theme }) => (!theme.rtl ? 'padding-left' : 'padding-right')}: 0;
        }
        @media only screen and (max-width: 767px) {
            font-size: 15px;
        }
        @media only screen and (max-width: 480px) {
            flex-flow: row;
        }
        .ant-steps-item-tail{
            @media only screen and (max-width: 480px) {
                background: #C5CAE1;
                top: 35px !important;
                padding: 20px 0 8px !important;
            }
        }
        .ant-steps-item-content{
            @media only screen and (max-width: 480px) {
                min-height: auto !important;
            }
        }
    }
    .steps-content{
        margin-top: 72px !important;
        @media only screen and (max-width: 1599px) {
            min-height: 252px !important;
        }
        @media only screen and (max-width: 1199px) {
            margin-top: 45px !important;
        }
        @media only screen and (max-width: 575px) {
            margin-top: 30px !important;
        }
    }
    .ant-steps-item-container{
        position: relative;
        &:after{
            position: absolute;
            ${({ theme }) => (!theme.rtl ? 'left' : 'right')}: 100%;
            top: 20px;
            color: #333;
            background-image: url(${require('../../../static/img/progress.svg')});
            width: 114%;
            height: 6px;
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            background-color: transparent !important;
            content: '';
            @media only screen and (max-width: 1399px) {
                width: 120%;
            }
            @media only screen and (max-width: 991px) {
                display: none;
            }
        }
        .ant-steps-item-tail{
            &:after{
                height: 80%;
            }
        }
    }
    .wizard-step-item{
        .ant-steps-item-container{
            &:after{
                background-image: url(${require('../../../static/img/progress.svg')});
            }
        }
    }
    .wizard-steps-item-active{
        .ant-steps-item-container{
            &:after{
                background-image: url(${require('../../../static/img/progress-active.svg')});
            }
        }
    }
    .success-step-item{
        .ant-steps-item-container{
            &:after{
                background-image: url(${require('../../../static/img/progress-success.svg')});
            }
        }
    }
    .ant-steps-item{
        /* overflow: hidden; */
        height: 100px;
        padding: ${({ theme }) => (theme.rtl ? '0 0 0 25px !important' : '0 25px 0 0 !important')} ;
        @media only screen and (max-width: 767px) {
            padding: 0 !important;
            &:not(:last-child){
                margin-bottom: 20px;
            }
        }
        &:last-child{
            ${({ theme }) => (!theme.rtl ? 'padding-left' : 'padding-right')}: 15px !important;
            @media only screen and (max-width: 767px) {
                padding: 0 !important;
            }
            .ant-steps-item-container{
                &:after{
                    display: none;
                }
            }
        }
        &:last-child{
            @media only screen and (max-width: 991px) {
                flex: 1 1;
            }
        }
        .ant-steps-item-title{
            font-size: 15px;
            font-weight: 500;
            margin-top: 8px;
            padding: ${({ theme }) => (theme.rtl ? '0 10px 0 0' : '0 0 0 10px')};
            
            color: ${({ theme }) => theme['gray-solid']} !important;
            @media only screen and (max-width: 1210px) {
                padding: ${({ theme }) => (!theme.rtl ? '0 0 0 20px' : '0 20px 0 0')};
            }
            @media only screen and (max-width: 767px) {
                padding: 0;
            }
            &:after{
                display: none;
                @media only screen and (max-width: 991px) {
                    display: none;
                }
            }
        }
        .ant-steps-item-icon{
            width: 50px;
            height: 50px;
            line-height: 50px;
            border: 0 none;
            box-shadow: 10px 0 20px ${({ theme }) => theme['gray-solid']}15;
            @media only screen and (max-width: 767px) {
                ${({ theme }) => (theme.rtl ? 'margin-left' : 'margin-right')}: 0;
                width: 35px;
                height: 35px;
                line-height: 35px;
            }
            .ant-steps-icon{
                font-size: 16px;
                font-weight: 500;
                color: ${({ theme }) => theme['dark-color']};
                @media only screen and (max-width: 767px) {
                    font-size: 15px;
                }
            }
        }
        &.ant-steps-item-active{
            .ant-steps-item-icon{
                .ant-steps-icon{
                    color: #fff;
                }
            }
            .ant-steps-item-title{
                color: ${({ theme }) => theme['dark-color']} !important;
            }
            &.ant-steps-item-finish{
                .ant-steps-item-title{
                    color: ${({ theme }) => theme['light-color']} !important;
                }
            }
        }
        &.ant-steps-item-finish{
            // .ant-steps-item-container{
            //     &:after{
            //         background-image: url(${require('../../../static/img/progress-active.svg')});
            //     }
            // }
            // .ant-steps-item-title{
            //     color: ${({ theme }) => theme['dark-color']} !important;
            //     &:after{
            //         background-image: url(${require('../../../static/img/progress-active.svg')});
            //     }
            // }
            .ant-steps-item-icon{
                background: ${({ theme }) => theme['success-color']} !important;
                .ant-steps-icon{
                    color: #fff;
                }
            }
        }
    }
    .basic-form-inner{
        width: 580px;
        @media only screen and (max-width: 575px){
            width: 100%
        }
        .ant-input-password.ant-input-affix-wrapper{
            ${({ theme }) => (!theme.rtl ? 'padding-left' : 'padding-right')}: 0;
        }
        .ant-form-item-label{
            label{
                font-weight: 400;
                color: ${({ theme }) => theme['dark-color']};
            }
        }
        .ant-form-item-control-input{
            .ant-input{
                padding: 12px 20px;
            }
        }
    }
    .steps-action{
        .btn-next{
            &:focus{
                background-color: ${({ theme }) => theme['primary-color']};
            }
        }
    }
    .atbd-form-checkout{
        .ant-input-affix-wrapper-rtl{
            input[type="password"]{
                ${({ theme }) => (theme.rtl ? 'padding-left' : 'padding-right')}: 15px;
            }
        }
       h1{
            font-size: 20px;
            font-weight: 500;
            margin-bottom: 46px;
            color: ${({ theme }) => theme['dark-color']};
            @media only screen and (max-width: 991px){
                font-size: 18px;
                margin-bottom: 22px;
            }
            @media only screen and (max-width: 479px){
                font-size: 16px;
            }
       }
       .ant-form-item-label{
           label{
               font-size: 15px;
           }
       }
       input::placeholder{
        color: ${({ theme }) => theme['extra-light-color']};
       }
       .input-message{
           margin-top: 6px;
           display: inline-block;
           font-size: 13px;
           color: ${({ theme }) => theme['gray-solid']};
       }

       .shipping-selection{
           > .ant-card{
               .ant-card-body{
                    border: 1px solid ${({ theme }) => theme['border-color-light']};
               }
           }
           .ant-card{
               .ant-card{
                   margin-bottom: 0 !important;
               }
           }
           .ant-radio-group {
               .ant-radio-wrapper{
                    display: flex;
                    align-items: flex-start;
                    span + span{
                        width: 100%;
                        min-height: 60px;
                    }
                   .ant-radio{
                        ${({ theme }) => (theme.rtl ? 'margin-left' : 'margin-right')}: 15px;
                    }
               }
           }
           .ant-form-item-control-input-content{
                input{
                    @media only screen and (max-width: 479px) {
                        width: 100% !important;
                        margin-bottom: 6px;
                    }
                }
               .input-leftText{
                   ${({ theme }) => (!theme.rtl ? 'margin-left' : 'margin-right')}: 10px;
                   @media only screen and (max-width: 479px) {
                        display: block;
                        ${({ theme }) => (!theme.rtl ? 'margin-left' : 'margin-right')}: 0px;
                    }
               }
           }
           .shipping-selection__card{
               .ant-card .ant-card{
                   border: 0 none;
                   border-radius: 20px;
                   box-shadow: 0 10px 30px ${({ theme }) => theme['light-color']}10;
               }
               .ant-radio-wrapper{
                    .ant-radio{
                        margin-top: 30px;
                    }
                    span + span{
                        padding: 0;
                    }
               }
               .cvv-wrap{
                   input{
                       max-width: 120px;
                   }
                   .input-leftText{
                       color: ${({ theme }) => theme['color-info']};
                   }
               }
            }
           .shipping-selection__paypal{
               margin-bottom: 20px;
               .ant-radio-wrapper{
                   span + span{
                       display: flex;
                       justify-content: space-between;
                       @media only screen and (max-width: 375px){
                            img{
                                display: none;
                            }
                        }
                   }
               }
           }
           .shipping-selection__paypal,
           .shipping-selection__cash{
                .ant-radio-wrapper{
                    align-items: center;
                    span + span{
                        font-size: 15px;
                        font-weight: 500;
                        padding: 0 25px;
                        display: flex;
                        align-items: center;
                        border-radius: 10px;
                        border: 1px solid ${({ theme }) => theme['border-color-normal']};
                    }
                }
           }
           .supported-card{
               align-items: center;
               justify-content: space-between;
               margin-bottom: 20px;
                @media only screen and (max-width: 479px) {
                    flex-flow: column;
                }
               .supported-card_logos{
                    @media only screen and (max-width: 479px) {
                        margin-top: 12px;
                    }
                   img + img{
                       ${({ theme }) => (!theme.rtl ? 'margin-left' : 'margin-right')}: 15px;
                   }
               }
           }
       }
   }
   .profile-hints{
       p{
        color: ${({ theme }) => theme['dark-color']};
           font-size: 15px;
           span{
               color: ${({ theme }) => theme['extra-light-color']};
           }
       }
   }
   .atbd-finish-order{
       max-width: 540px;
       margin: 0 auto;
       padding: 30px;
       min-height: 248px;
       border-radius: 6px;
       border: 1px solid ${({ theme }) => theme['bg-color-deep']};
       h1,
       h2,
       h3,
       h4,
       h5,
       h6{
            margin-bottom: 16px;
            color: ${({ theme }) => theme['darks-color']};
       }
       .ant-checkbox{
           &:hover{
               .ant-checkbox-inner{
                    border-color: ${({ theme }) => theme['success-color']};
               }
           }
       }
       .ant-checkbox-checked{
           &:after{
                border-color: ${({ theme }) => theme['success-color']};
           }
           .ant-checkbox-inner{
                background-color: ${({ theme }) => theme['success-color']};
                border-color: ${({ theme }) => theme['success-color']};
           }
       }
       .ant-checkbox-input{
           &:focus + .ant-checkbox-inner{
            border-color: ${({ theme }) => theme['success-color']};
           }
       }
       .checkbox-label{
            ${({ theme }) => (theme.rtl ? 'margin-right' : 'margin-left')}: 10px;
            font-size: 15px;
            color: ${({ theme }) => theme['extra-light-color']};
       }
   }
   .atbd-review-order{
       > .ant-card{
           > .ant-card-body{
               border: 1px solid ${({ theme }) => theme['border-color-light']};
           }
       }
       h1{
           font-size: 20px;
           font-weight: 500;
           margin-bottom: 45px;
           @media only screen and (max-width: 479px) {
                font-size: 16px;
                margin-bottom: 25px;
            }
       }

       .atbd-review-order__single{
           .ant-radio-wrapper{
                display: flex;
                align-items: flex-start;
            }
            .ant-card{
                .ant-card-body{
                    padding: 30px !important;
                    border-radius: 15px;
                    box-shadow: 0 10px 30px ${({ theme }) => theme['gray-solid']}10;
                }
            }
            h1{
                font-size: 18px;
                font-weight: 400;
                margin-bottom: 30px;
            }
            .method-info{
                margin-top: -2px;
                font-weight: 500;
                color: color: ${({ theme }) => theme['dark-color']};
                img{
                    margin-top: -4px;
                    max-width: 40px;
                }
            }
            .btn-addCard{
                font-weight: 500
                display: inline-block;
                font-size: 13px;
                margin-top: 20px;
                color: ${({ theme }) => theme['info-color']};
            }

            .table-cart{
                border-bottom: 1px solid ${({ theme }) => theme['border-color-light']};
                .cart-single-t-price{
                    color: ${({ theme }) => theme['gray-color']};
                }
                .ant-table-content{
                    padding-bottom: 10px;
                }
                thead{
                    display: none;
                }
                .ant-table-tbody{
                    .ant-table-row{
                        &:hover{
                            box-shadow: 0 0;
                        }
                    }
                    >tr >td{
                        padding: 8px 15px;
                        &:first-child{
                            ${({ theme }) => (!theme.rtl ? 'padding-left' : 'padding-right')}: 0px;
                        }
                        &:last-child{
                            ${({ theme }) => (!theme.rtl ? 'padding-left' : 'padding-right')}: 0px;
                            text-align: ${({ theme }) => (theme.rtl ? 'left' : 'right')};
                        }
                    }
                }
            }
            .cart-single{
                .cart-single__info{
                    h1{
                        color: ${({ theme }) => theme['dark-color']};
                        margin-bottom: 8px;
                    }
                }
            }
       }

        .atbd-review-order__shippingTitle{
            h1{
                display: flex;
                margin-bottom: 30px;
                justify-content: space-between;
                color: ${({ theme }) => theme['gray-color']};
                @media only screen and (max-width: 479px) {
                    flex-flow: column;
                }
                a{
                    font-size: 14px;
                    display: inline-flex;
                    align-items: center;
                    @media only screen and (max-width: 479px) {
                        margin-top: 12px;
                    }
                    svg{
                        width: 14px;
                        height: 14px;
                        ${({ theme }) => (theme.rtl ? 'margin-left' : 'margin-right')}: 4px;
                    }
                }
            }
        }

        .atbd-review-order__shippingInfo{
            .shipping-info-text{
                margin: -4px 12px 0;
                h1{
                    font-size: 15px;
                    font-weight: 500;
                    margin-bottom: 8px;
                }
                p{
                    font-size: 15px;
                    color: ${({ theme }) => theme['gray-color']};
                }
            }
            .btn-addNew{
                font-size: 13px;
                font-weight: 500;
                color: ${({ theme }) => theme['info-color']};
            }
        }
   }
   .invoice-summary-inner{
        .summary-total{
            margin-bottom: 0;
        }
   }
   .summary-list{
        margin: 20px 0 10px !important;
        .summary-list-text{
            font-size: 15px;
            color: ${({ theme }) => theme['gray-color']};
        }
        li{
            &:not(:last-child){
                margin-bottom: 10px;
            }
        }
   }

   .checkout-successful{
       text-align: center;
        > .ant-card{
            > .ant-card-body{
                border: 1px solid ${({ theme }) => theme['border-color-light']};
            }
        }
        .ant-card {
            box-shadow: 0 10px 30px ${({ theme }) => theme['light-color']}10;
            .ant-card{
                padding: 25px;
                    margin-bottom: 0 !important;
            }
        }
        .icon-success{
            width: 34px;
            height: 34px;
            margin: 0 auto 16px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #fff;
            background: ${({ theme }) => theme['success-color']};
            svg{
                width: 18px;
            }
        }
        h1{
            font-size: 20px;
            font-weight: 500;
            margin-bottom: 16px;
        }
        p{
            margin-bottom: 0;
            color: ${({ theme }) => theme['gray-color']};
        }
   }
`;

const ProductTable = Styled.div`
    .table-cart{
        .ant-table-content{
            padding-bottom: 10px;
        }
        .ant-table-tbody{
            .cart-single{
                figure{
                    align-items: center;
                    margin-bottom: 0;
                    img{
                        max-width: 80px;
                        min-height: 80px;
                        border-radius: 10px;
                        ${({ theme }) => (theme.rtl ? 'margin-left' : 'margin-right')}: 25px;
                    }
                }
                .cart-single__info{
                    h1,
                    h2,
                    h3,
                    h4,
                    h5,
                    h6{
                        font-size: 15px;
                        font-weight: 500;
                    }
                    p{
                        margin-bottom: 0;
                    }
                }
            }
            .ant-table-row{
                &:hover{
                    box-shadow: 0 10px 15px ${({ theme }) => theme['light-color']}15;
                    td{
                        background: #fff !important;
                    }
                    .table-action{
                        button{
                            background: #FF4D4F15;
                            i,
                            svg{
                                color: ${({ theme }) => theme['danger-color']};
                            }
                        }
                    }
                }
            }
        }
    }
    .table-invoice{
        .ant-table table {
            text-align: ${({ theme }) => (theme.rtl ? 'right' : 'left')}
        }
        table{
            thead{
                >tr{
                    th{
                        border-top: 1px solid ${({ theme }) => theme['border-color-light']};
                        border-bottom: 1px solid ${({ theme }) => theme['border-color-light']};
                        &:first-child{
                            ${({ theme }) => (!theme.rtl ? 'border-left' : 'border-right')}: 1px solid ${({ theme }) =>
  theme['border-color-light']};
                        }
                        &:last-child{
                            ${({ theme }) => (theme.rtl ? 'border-left' : 'border-right')}: 1px solid ${({ theme }) =>
  theme['border-color-light']};
                            text-align: ${({ theme }) => (theme.rtl ? 'left' : 'right')};
                        }
                    }
                }
            }
            tbody{
                >tr{
                    &.ant-table-row{
                        &:hover{
                            >td{
                                background: #fff;
                            }
                        }
                    }
                    td{
                        border-bottom: 1px solid ${({ theme }) => theme['border-color-light']};
                        color: ${({ theme }) => theme['gray-color']};
                        border-radius: 0 !important;
                        &:last-child{
                            text-align: ${({ theme }) => (theme.rtl ? 'left' : 'right')};
                        }
                        .product-info-title{
                            font-size: 15px;
                            font-weight: 500;
                            color: ${({ theme }) => theme['dark-color']};
                        }
                        .product-unit{
                            ${({ theme }) => (!theme.rtl ? 'padding-left' : 'padding-right')}: 40px;
                        }
                        .product-quantity{
                            ${({ theme }) => (!theme.rtl ? 'padding-left' : 'padding-right')}: 50px;
                        }
                    }
                }
            }
        }
        .product-info{
            min-width: 300px;
            .product-info{
                margin-bottom: 8px;
            }
        }
    }
    table{
        thead{
            tr{
                border-radius: 10px;
                th{
                    border-bottom: 0 none;
                    background:  ${({ theme }) => theme['bg-color-light']};
                    &:first-child{
                    border-radius: ${({ theme }) => (theme.rtl ? '0 10px 10px 0' : '10px 0 0 10px')} !important;
                    }
                    &:last-child{
                        border-radius: ${({ theme }) => (!theme.rtl ? '0 10px 10px 0' : '10px 0 0 10px')} !important;
                    }
                }
            }
        }
        tbody{
            tr{
                border-radius: 10px;
                td{
                    border-bottom: 0 none;
                    &:first-child{
                        border-radius: ${({ theme }) => (theme.rtl ? '0 10px 10px 0' : '10px 0 0 10px')};
                    }
                    &:last-child{
                        border-radius: ${({ theme }) => (!theme.rtl ? '0 10px 10px 0' : '10px 0 0 10px')} !important;
                    }
                }
            }
        }

        .info-list{
            li{
                display: inline-block;
                &:not(:last-child){
                    ${({ theme }) => (theme.rtl ? 'margin-left' : 'margin-right')}: 20px;
                }
                span{
                    font-size: 14px;
                    color: ${({ theme }) => theme['gray-color']};
                    &.info-title{
                        ${({ theme }) => (theme.rtl ? 'margin-left' : 'margin-right')}: 5px;
                        font-weight: 500;
                        color: ${({ theme }) => theme['dark-color']};
                    }
                }
            }
        }
        .cart-single-price{
            font-size: 15px;
            color: ${({ theme }) => theme['gray-color']};
        }
        .cart-single-t-price{
            font-size: 15px;
            font-weight: 500;
            color: ${({ theme }) => theme['primary-color']};
            display: inline-block;
            min-width: 80px;
        }
        .cart-single-quantity{
            button{
                background-color: ${({ theme }) => theme['bg-color-normal']};
                i,
                img,
                svg{
                    min-width: 12px;
                    height: 12px;
                }
                &.btn-inc,
                &.btn-dec{
                    width: 36px;
                    height: 36px;
                }
                &.btn-inc{
                    ${({ theme }) => (!theme.rtl ? 'margin-left' : 'margin-right')}: 16px;
                    @media only screen and (max-width: 575px){
                        ${({ theme }) => (!theme.rtl ? 'margin-left' : 'margin-right')}: 10px;
                    }
                }
                &.btn-dec{
                    ${({ theme }) => (theme.rtl ? 'margin-left' : 'margin-right')}: 16px;
                    @media only screen and (max-width: 575px){
                        ${({ theme }) => (theme.rtl ? 'margin-left' : 'margin-right')}: 10px;
                    }
                }
            }
        }
        .table-action{
            text-align: ${({ theme }) => (theme.rtl ? 'left' : 'right')};
            button{
                padding: 0 11px;
                height: 38px;
                background: #fff;
                i,
                svg{
                    color: #707070;
                }
            }
        }
    }
`;

const Wizard = Styled.div`
    display: flex;
    align-items: flex-start;
    @media only screen and (max-width: 991px) {
        flex-flow: column;
    }
    .steps-wrapper{
        @media only screen and (max-width: 991px) {
            padding: 25px 0 !important;
            width: 100%;
            margin-top: -50px;
            ${({ theme }) => (theme.rtl ? 'border-right' : 'border-left')}: 0 !important;
        }
    width: 70%;
    }
    .basic-form-inner{
        width: 100%;
        @media only screen and (max-width: 1599px) {
            width: 100%;
        }
    }
    .ant-steps-item-container {
        position: relative;
        width: 100% !important;
        align-items: flex-start !important;
        @media only screen and (max-width: 1399px) {
            ${({ theme }) => (theme.rtl ? 'padding-right' : 'padding-left')}: 0 !important;
        }
        .ant-steps-item-tail{
            display: none !important;
        }
    }
    .ant-steps-item:last-child {
        ${({ theme }) => (theme.rtl ? 'padding-right' : 'padding-left')}: 0 !important;
    }
    .ant-steps {
        max-height: 600px;
        overflow-x: hidden;
        overflow-y: scroll;
        flex: 1;
        position: relative;
        min-width: 450px;
        flex-direction: column;
        ${({ theme }) => (theme.rtl ? 'padding-right' : 'padding-left')}: 0 !important;
        @media only screen and (max-width: 1599px) {
            min-width: auto;
        }
        @media only screen and (max-width: 767px) {
            align-items: flex-start;
            padding: 25px 0 !important;
        }
        .ant-steps-item-container:after {
            display: none;
        }
        .ant-steps-item {
            border-radius: 5px;
            padding: 16px 30px 18px 30px !important;
            @media only screen and (max-width: 1599px) {
                min-width: 200px;
            }
            &:not(:last-child){
                margin-bottom: 10px;
            }
        }
        .ant-steps-item-content{
            .ant-steps-item-title{
                margin-top: 0px;
                line-height: 1.4;
                ${({ theme }) => (theme.rtl ? 'padding-right' : 'padding-left')}: 0;
                h2{
                    font-size: 16px;
                    margin-bottom: .35rem;
                    color: ${({ theme }) => theme['dark-color']};
                }
                p{
                    font-size: 13px;
                    font-weight: 400;
                    margin-bottom: 0;
                    color: ${({ theme }) => theme['gray-color']};
                }
            }
        }
    }
    .steps-content{
        margin-top: 0 !important;
        .basic-form-inner{
            &.theme-light{
                .ant-input,
                .ant-select-selector,
                .ant-input-password{
                    border: 0 none;
                    background-color: #F4F5F7;
                }
            }
            .ant-form-item-label{
                white-space: break-spaces;
            }
            .shipping-form,
            .payment-method-form {
                min-width: 530px;
                @media only screen and (max-width: 767px) {
                    min-width: 100%;
                }
            }
        }
    }
    .ant-steps:not(.ant-steps-dot):not(.ant-steps-navigation) .ant-steps-item .ant-steps-item-icon {        
        display: none;
    }
    .ant-steps-item.ant-steps-item-process.ant-steps-item-active {
        background: #F4F5F7;
        //height: 70px;
    }

    .step-action-wrap{
        border-top: 1px solid ${({ theme }) => theme['bg-color-deep']}; 
        margin-top: 30px;
        .step-action-inner{
            width: 100% !important;
            padding: 0 !important;
            @media only screen and (max-width: 1399px) {
                width: 100%;
            }
        }
        .steps-action{
            margin-top: 40px;
        }
    }
    .atbd-review-order{
        >.ant-card{
            margin-bottom: 0 !important;
        }
        &.theme-light{

            > .ant-card > .ant-card-body{
                border: 0 none;
                padding: 0 !important;
            }
            .atbd-review-order__single{
                &:last-child{
                    .ant-card{
                        margin-bottom: 0 !important;
                    }
                }
                .ant-card-body{
                    background-color: #F4F5F7;
                    box-shadow: 0 0;
                }
                .ant-table{
                    background-color: transparent;
                }
                .table-cart {
                    @media only screen and (max-width: 1799px) {
                        max-width: 560px;
                    }
                    @media only screen and (max-width: 1699px) {
                        max-width: 500px;
                    }
                    @media only screen and (max-width: 1599px) {
                        max-width: 100%;
                    }
                    @media only screen and (max-width: 1399px) {
                        max-width: 410px;
                    }
                    .ant-table-tbody .ant-table-row:hover td{
                        background-color: transparent !important;
                    }
                    .cart-single-quantity{
                        button{
                            background-color: #fff;
                        }
                    }
                }
            }
        }
    }
    .shipping-selection__card{
        .ant-card{
            margin-bottom: 0 !important;
        }
        .ant-card-body{
            padding: 0 !important;
        }
    }
    .ex-date-year-wrap{
        .ex-date{
            ${({ theme }) => (theme.rtl ? 'margin-left' : 'margin-right')}: 20px;
            flex: 0 0 48%
        }
        .ex-year{
            flex: 0 0 48%
        }
    }
    
`;
const WizardBlock = Styled.div`
    
    >.ant-card{
        .ant-card-body{
            padding: 50px 0 !important;
        }
        .steps-content{
            .ant-card-body{
                padding: 25px !important;
            }
        }
    }
    
`;
const OrderSummary = Styled.div`
    max-width: 650px;
    margin: 0 auto;
    .ant-card{
        margin-bottom: 0 !important;
    }
    .ant-card-body{
        box-shadow: 0 10px 30px ${({ theme }) => theme['dark-color']}10;
    }
    .ant-form-item{
        margin-bottom: 0;
    }

    .summary-table-title{
        font-size: 18px;
        font-weight: 500;
        margin-bottom: 25px;
        color: ${({ theme }) => theme['dark-color']};
    }
    .order-summary-inner{
        padding-bottom: 5px;
        @media only screen and (max-width: 1599px){
            max-width: 600px;
            margin: 0 auto;
        }
        .ant-select{
            .ant-select-selection-item{
                font-weight: 500;
            }
        }
    }
    .invoice-summary-inner{
        .summary-list{
            margin: 22px 0;
            li{
                &:not(:last-child){
                    margin-bottom: 12px;
                }
            }
        }
        .summary-total-amount{
            color: ${({ theme }) => theme['primary-color']} !important;
        }
    }

    .summary-list{
        li{
            display: flex;
            justify-content: space-between;
            &:not(:last-child){
                margin-bottom: 18px;
            }
            span{
                font-weight: 500;
            }
            .summary-list-title{
                color: ${({ theme }) => theme['gray-color']};
            }
            .summary-list-text{
                color: ${({ theme }) => theme['dark-color']};
            }
        }
    }
    .ant-select-focused.ant-select-single{
        .ant-select-selector{
            box-shadow: 0 0 !important;
        }
    }
    .ant-select-single{
        margin-top: 18px;
        .ant-select-selection-search-input{
            height: fit-content;
        }
        .ant-select-selector{
            padding: 0 !important;
            border: 0 none !important;
            color: ${({ theme }) => theme['success-color']};
        }
        .ant-select-arrow{
            ${({ theme }) => (theme.rtl ? 'left' : 'right')}: 0;
        }
    }
    .promo-apply-form{
        display: flex;
        align-items: flex-end;
        margin: 5px 0 18px;
        @media only screen and (max-width: 479px){
            flex-flow: column;
            align-items: flex-start;
        }
        .ant-form-item{
            margin-bottom: 0;
        }
        .ant-row{
            flex: auto;
            flex-flow: column;
        }
        .ant-form-item-label{
            text-align: ${({ theme }) => (!theme.rtl ? 'left' : 'right')};
            label{
                font-weight: 400;
                margin-bottom: 4px;
                height: fit-content;
                color: ${({ theme }) => theme['gray-color']};
            }
        }
        .ant-form-item-control-input-content{
            display: flex;
            @media only screen and (max-width: 479px){
                flex-flow: column;
            }
            input{
                margin: ${({ theme }) => (theme.rtl ? '0 0 0px 6px' : '0 6px 0px 0')};
                height: 40px;
                @media only screen and (max-width: 479px){
                    margin: ${({ theme }) => (theme.rtl ? '0 0 10px 6px' : '0 6px 10px 0')};
                    width: 100% !important;
                }
            }
            button{
                height: 40px;
            }
        }
    }
    .summary-total{
        display: inline-flex;
        justify-content: space-between;
        width: 100%;
        .summary-total-label{
            font-size: 16px;
            font-weight: 500;
            color: ${({ theme }) => theme['dark-color']};
        }
        .summary-total-amount{
            font-size: 18px;
            font-weight: 600;
            color: ${({ theme }) => theme['primary-color']};
        }
    }
    .btn-proceed{
        font-size: 15px;
        font-weight: 500;
        width: 100%;
        height: 50px;
        border-radius: 8px;
        margin-top: 22px;
        @media only screen and (max-width: 575px){
            font-size: 13px;
        }
        a{
            display: flex;
            align-items: center;
        }
        i,
        svg{
            ${({ theme }) => (!theme.rtl ? 'margin-left' : 'margin-right')}: 6px;
        }
    }
`;

export { WizardBlock, Wizard, WizardWrapper, ProductTable, OrderSummary };
